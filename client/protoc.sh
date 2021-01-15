#!/usr/bin/env bash

# Create build folder for javascript.
mkdir build && cd build && mkdir proto && cd ../

# Create src proto directory for typescript.
cd src
mkdir proto
cd ../

protoc \
    --proto_path=./../protos/src/main/proto/io/grpc/examples/animals/ \
    --plugin=protoc-gen-ts=./node_modules/.bin/protoc-gen-ts \
    --ts_out=service=grpc-web:./src/proto \
    --js_out=import_style=commonjs,binary:./build/proto \
    dog.proto