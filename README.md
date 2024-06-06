# servicio-cuenta-cliente y servicio-cuenta-movimiento-banco

Microservicio de Clientes, cuentas y movimiento asincrónica

# Definicion de la Arquitectura principal para comunicación de Microservicios por Eventos

![image-modeloArquitecturaEventBus](img/SoftwareArchitectureEventBus.png)

Para iniciar estos pasos tener las dos imágenes docker:

### Probar en un ambiente local desde:

### Url GitHub
- https://github.com/santbetv/service-client-pichincha.git
- https://github.com/santbetv/service-account-movements.git

El proyecto corre sobre un db H2 para probar más rápido, pero también se adjunta
SQL para probar en db postgreSQL.

### Postman

Archivo Postman que permite testear las funcionalidades.

### Url swagger

Url de servicios para validar desde swagger funcionalidades más rápidas.

- http://localhost:8089/api/swagger-ui/index.html
- http://localhost:8090/api/swagger-ui/index.html


# Conectar con rabbitMQ manejando colas, para funcionar con clientes, cuentas y movimientos.

## Tener descargado rabbitMQ con: 

### latest RabbitMQ 3.12
- docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3.12-management

## Crear colas
- my_queue_cliente
- my_queue_cuenta
