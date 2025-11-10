#!/bin/sh
set -e
mkdir -p docker/secrets
# Generate example secrets (do NOT commit secrets to VCS)
# Replace content as needed
openssl rand -base64 32 > docker/secrets/jwt_key_trip_management
openssl rand -base64 32 > docker/secrets/jwt_key_hotel_tracking
openssl rand -base64 32 > docker/secrets/jwt_key_flight_tracking
openssl rand -base64 32 > docker/secrets/jwt_key_experience_tracking
openssl rand -base64 32 > docker/secrets/jwt_key_carhire_tracking
openssl rand -base64 32 > docker/secrets/jwt_key_expense_management
openssl rand -base64 32 > docker/secrets/jwt_key_userprofile

openssl rand -base64 16 > docker/secrets/pg_pwd_trip_management
openssl rand -base64 16 > docker/secrets/pg_pwd_hotel_tracking
openssl rand -base64 16 > docker/secrets/pg_pwd_flight_tracking
openssl rand -base64 16 > docker/secrets/pg_pwd_experience_tracking
openssl rand -base64 16 > docker/secrets/pg_pwd_carhire_tracking
openssl rand -base64 16 > docker/secrets/pg_pwd_expense_management
openssl rand -base64 16 > docker/secrets/pg_pwd_userprofile

echo "Secrets generated under docker/secrets/ (DO NOT commit these files)."