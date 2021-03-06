# FunctionDemo

## 概要
AWS LambdaにてSpringCloudFunctionの実装サンプルです  
ビルドデプロイのパターンが以下の３とおり試しています。
- Native-imageを作成してコンテナイメージをデプロイする場合
- Native-fileを作成してzipでデプロイする場合
- Native-fileを含むLambdaカスタムランタイムイメージを作成し、デプロイする場合

### Native-imageを作成してコンテナイメージをデプロイする場合
maven pluginでNative-imageの作成
~~~
./mvnw spring-boot:build-image
~~~
起動
~~~
docker run --rm -p 8080:8080 hello-function:1.0.0
~~~
ECRに登録

### Native-fileを作成してzipでデプロイする場合
ネイティブビルド用のコンテナの起動・コンテナ内でビルド
https://www.graalvm.org/22.0/docs/getting-started/container-images/
~~~
docker rm -f build-graalvm && \
docker run -v $(pwd):/build-work -v ~/.m2:/root/.m2 -it --name build-graalvm ghcr.io/graalvm/graalvm-ce:latest bash -c "cd /build-work && ./mvnw clean package -D skipTests -P local && chmod 777 -R target"
~~~
※-P localを-P lambdaに変更するとNettyが起動しないLambda用のバイナリになる

2回目以降
~~~
docker start i- build-graalvm
~~~

実行
~~~
./target/hello-function
./target/hello-function --spring.profiles.active=develop-local
~~~


ラムダのデプロイ
ラムダは事前にカスタムラインタイム「Custom runtime on Amazon Linux 2」ハンドラ「org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest」で作成しておく
org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest

AWS CLIのインストール
~~~
curl "https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip" -o "awscliv2.zip"
unzip awscliv2.zip
sudo ./aws/install
~~~

AWS認証情報を設定しておく
~~~
aws configure
~~~

~~~
FUNCTION_NAME=hello-function
ALIAS=1-0-0
# publishをつけて新バージョンのlambdaを払い出し
PUBLISH_RESULT=$(aws lambda update-function-code --function-name ${FUNCTION_NAME} --zip-file fileb://target/hello-function-1.0.0-SNAPSHOT-native-zip.zip --publish)
# 最新バージョンの取得
LAMBDA_TARGET_VERSION=$(echo ${PUBLISH_RESULT} | jq -r .Version)
# エイリアスに最新バージョンを紐づけ
aws lambda update-alias --function-name ${FUNCTION_NAME} --name ${ALIAS} --function-version ${LAMBDA_TARGET_VERSION}
~~~

API-GATEWAY呼び出し
~~~
API_ID=XXXXXX
API_VER=1-0-0
API_KEY=XXXXXXXX
curl --location --request POST 'https://${API_ID}.execute-api.ap-northeast-1.amazonaws.com/API_VER/hello' \
--header 'x-api-key: ${API_KEY}' \
--header 'Content-Type: application/json' \
--data-raw '{"message":"こんにちは"}'
~~~


### Native-fileを含むLambdaカスタムランタイムイメージを作成する場合
Dockerfileからイメージを作成
~~~
GITHUB_TOKEN=＜GitHubのトークン＞
docker build . -t hello-function:latest --progress=plain --no-cache  --build-arg GITHUB_TOKEN=${GITHUB_TOKEN}
~~~

起動
~~~
docker run -p 9000:8080 hello-function:latest
~~~
呼出
https://docs.aws.amazon.com/ja_jp/lambda/latest/dg/images-test.html
~~~
curl -XPOST "http://localhost:9000/2015-03-31/functions/function/invocations" -d '{"message": "こんにちは"}'
~~~

### Dockerコンテナの削除
~~~
docker rm -f `docker ps -a -q`
~~~

### Dockerイメージの削除
~~~
docker rmi `docker images -q`
docker image prune
~~~

### ECRへのイメージ登録
~~~
TAG=latest
ACCOUNT_ID=＜AWSアカウントID＞
# ログイン
aws ecr get-login-password --region ap-northeast-1 | docker login --username AWS --password-stdin ${ACCOUNT_ID}.dkr.ecr.ap-northeast-1.amazonaws.com
# imageのbuild
docker build -t hello-function:${TAG} .
# タグ作成
docker tag function-demo:${TAG} ${ACCOUNT_ID}.dkr.ecr.ap-northeast-1.amazonaws.com/function-demo:${TAG}
# ECRにpush
docker push ${ACCOUNT_ID}.dkr.ecr.ap-northeast-1.amazonaws.com/function-demo:${TAG}
~~~



### 参考URL
Spring Native公式
https://docs.spring.io/spring-native/docs/current/reference/htmlsingle/#overview

GraalVMを使用したFastSpring Boot AWS Lambdas
https://dzone.com/articles/fast-spring-boot-aws-lambdas-with-graalvm

lambdaカスタムランタイムイメージ
https://hub.docker.com/r/amazon/aws-lambda-provided

カスタムランタイム の Lambda をコンテナイメージ化してみた
https://sadayoshi-tada.hatenablog.com/entry/2021/01/29/090600

カスタムdockerイメージでlambdaを動かす
https://qiita.com/ohr486/items/0d8bea82cf294ad1e632

大量リクエストを低コストでさばく AWS Lambda 関数を JVM で実現
https://aws.amazon.com/jp/builders-flash/202110/jvm-lambda-function/?awsf.filter-name=*all

sudoをパスワードなしで
https://qiita.com/RyodoTanaka/items/e9b15d579d17651650b7

https://spring.pleiades.io/spring-boot/docs/current/reference/html/howto.html