#!/bin/bash

# ビルド
# ./mvnw clean package -P lambda -D skipNativeTests
./mvnw clean package -P lambda -D skipTests

FUNCTION_NAME=hello-function
ALIAS=1-0-0
# publishをつけて新バージョンのlambdaを払い出し
PUBLISH_RESULT=$(aws lambda update-function-code --function-name ${FUNCTION_NAME} --zip-file fileb://target/hello-function-1.0.0-SNAPSHOT-native-zip.zip --publish)
# 最新バージョンの取得
LAMBDA_TARGET_VERSION=$(echo ${PUBLISH_RESULT} | jq -r .Version)
# エイリアスに最新バージョンを紐づけ
aws lambda update-alias --function-name ${FUNCTION_NAME} --name ${ALIAS} --function-version ${LAMBDA_TARGET_VERSION}