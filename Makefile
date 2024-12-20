include .env

# create a .env file with all environment variable or export them before launching
# and don't forget to add .env to .gitignore :)

# Env. variables needed:
# STUDENT_ISU=sXXXXXX


minio-up:
	docker-compose up --build -d --remove-orphans

port-forward:
	@echo "Port forwarding to Helios..."
	@ssh -L 5432:localhost:5432 -p 2222 ${STUDENT_ISU}@helios.se.ifmo.ru

