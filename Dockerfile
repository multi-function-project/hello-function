## 1.AmazonLinuxにGraalVMをインストール、ビルドしてLambdaカスタムイメージに配置するパターン
# FROM amazonlinux:2 as build-image

# ARG GITHUB_TOKEN

# RUN yum -y update \
#     && yum install -y tar gzip bzip2-devel ed gcc gcc-c++ gcc-gfortran \
#     less libcurl-devel openssl openssl-devel readline-devel xz-devel \
#     zlib-devel glibc-static libcxx libcxx-devel llvm-toolset-7 zlib-static \
#     && rm -rf /var/cache/yum

# ENV GRAAL_VERSION 22.0.0.2
# ENV GRAAL_FOLDERNAME graalvm-ce-java11-${GRAAL_VERSION}
# ENV GRAAL_FILENAME graalvm-ce-java11-linux-amd64-${GRAAL_VERSION}.tar.gz
# RUN curl -4 -L https://github.com/graalvm/graalvm-ce-builds/releases/download/vm-${GRAAL_VERSION}/${GRAAL_FILENAME} | tar -xvz
# RUN mv $GRAAL_FOLDERNAME /usr/lib/graalvm
# RUN rm -rf $GRAAL_FOLDERNAME

# # Graal maven plugin requires Maven 3.3.x
# ENV MVN_VERSION 3.6.3
# ENV MVN_FOLDERNAME apache-maven-${MVN_VERSION}
# ENV MVN_FILENAME apache-maven-${MVN_VERSION}-bin.tar.gz
# RUN curl -4 -L https://mirrors.ukfast.co.uk/sites/ftp.apache.org/maven/maven-3/${MVN_VERSION}/binaries/${MVN_FILENAME} | tar -xvz
# RUN mv $MVN_FOLDERNAME /usr/lib/maven
# RUN rm -rf $MVN_FOLDERNAME

# VOLUME /project
# WORKDIR /project

# RUN /usr/lib/graalvm/bin/gu install native-image
# RUN ln -s /usr/lib/graalvm/bin/native-image /usr/bin/native-image
# RUN ln -s /usr/lib/maven/bin/mvn /usr/bin/mvn

# ENV JAVA_HOME /usr/lib/graalvm

# # ENTRYPOINT ["sh"]

# # WORKDIR "/task"
# COPY ./ ./ 

# # Native Build
# RUN ./mvnw -s settings.xml -Denv.GITHUB_TOKEN=GITHUB_TOKEN clean package -P lambda -f pom.xml

# FROM public.ecr.aws/lambda/provided:al2
# COPY --from=build-image /project/src/shell/bootstrap ${LAMBDA_RUNTIME_DIR}
# COPY --from=build-image /project/target/hello-function ${LAMBDA_TASK_ROOT}

# CMD ["org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"]

## 2. GraalVMイメージビルドしてLambdaカスタムイメージに配置するパターン
# https://github.com/graalvm/container/pkgs/container/graalvm-ce
# FROM ghcr.io/graalvm/graalvm-ce:ol8-java11 as build-image

# ARG GITHUB_TOKEN

# WORKDIR "/task"
# COPY ./ ./ 

# # Native Build
# RUN ./mvnw clean package -P lambda -f pom.xml -s settings.xml -Denv.GITHUB_TOKEN=GITHUB_TOKEN

# FROM public.ecr.aws/lambda/provided:al2

# COPY --from=build-image /task/src/shell/bootstrap ${LAMBDA_RUNTIME_DIR}
# COPY --from=build-image /task/target/hello-demo ${LAMBDA_TASK_ROOT}

# CMD ["org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"]

## 3. 事前にビルドしておくパターン
FROM public.ecr.aws/lambda/provided:al2

COPY src/shell/bootstrap ${LAMBDA_RUNTIME_DIR}
COPY target/hello-function ${LAMBDA_TASK_ROOT}

CMD ["org.springframework.cloud.function.adapter.aws.FunctionInvoker::handleRequest"]