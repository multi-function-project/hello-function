##!/bin/bash

API_ID=XXXXXX
API_VER=1-0-0
API_KEY=XXXXXXXX
curl --location --request POST 'https://${API_ID}.execute-api.ap-northeast-1.amazonaws.com/API_VER/hello' \
--header 'x-api-key: ${API_KEY}' \
--header 'Content-Type: application/json' \
--data-raw '{"message":"こんにちは"}'