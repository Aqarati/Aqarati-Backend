# Start with a base Go image
FROM golang:1.17-alpine as builder

# Set necessary environment variables needed for our image
ENV GO111MODULE=on \
    CGO_ENABLED=0 \
    GOOS=linux \
    GOARCH=amd64

# Move to working directory /build
WORKDIR /build

# Copy and download dependency using go mod
COPY go.mod .
COPY go.sum .
RUN go mod download

# Copy the code into the container
COPY . .

# Build the application
RUN go build -o main .

# Start a new stage from scratch
FROM alpine:latest

# Set necessary environment variables
ENV GIN_MODE=release

# Install necessary packages
RUN apk update && apk add --no-cache ca-certificates

# Copy the Pre-built binary file from the previous stage
COPY --from=builder /build/main /app/

# Copy the .env file to the container
COPY .env /app/

# Expose port 8080 to the outside world
EXPOSE 8080

# Command to run the executable
CMD ["./app/main"]
