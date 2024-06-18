# GymApp


GymApp es una aplicación para gestionar entrenamientos de pesas. Fue creada con el propósito de crear 
mi primero proyecto con Spring Boot y además poder tener mi propia app para anotar mis entrenameintos 
tanto en dispositivos moviles como en pc. Está en fases tempranas, por lo que aun faltan funciones que
se incorporarán en el futuro.



## Tecnologías Utilizadas

- Java
- Spring Boot
- Spring Security
- Hibernate
- MySQL
- Thymeleaf

## Instalación

### 1. Clonar el repositorio:
   ```bash
   git clone https://github.com/NyuAlpha/GymApp.git
   cd gymapp
   ```



### 2. Configurar base de datos:

   Crea una base de datos MySql en local con la siguiente base de datos:

      CREATE DATABASE gymapp;

   Ve a la raíz del proyecto y en el archivo application-dev.properties modifica
   la sección siguiente para que se adapte a tu base de datos

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:puerto/gymapp
   spring.datasource.username=usuario
   spring.datasource.password=contraseña
   ```



### 3. Crear ejecutable:

   Para crear el ejecutable jar:
   ```bash
   ./mvnw clean package
   ```




### 4. Ejecutar sin empaquetar:

   Primera vez que se ejecuta o se desea resetear las tablas de la base de datos:

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.jpa.hibernate.ddl-auto=create"
   ```

   Una vez existen las tablas:

   ```bash
   ./mvnw spring-boot:run
   ```




### 5. Ejecutar en local:


   Una vez se crea el jar ya puedes ejecutar el proyecto, pero la primera vez que lo ejecutes debes usar:

   ```bash
   java -jar target/gymapp-alpha-1.0.jar --spring.jpa.hibernate.ddl-auto=create
   ```

   Eso creará las tablas de forma automática y dos usuarios, y una vez hecho, bastará con usar:

   ```bash
   java -jar target/gymapp.jar
   ```


   Los usuarios creados de forma local son:
      username = user   password = 123456
      username = admin  password = 123456
   aunque pueden ser modificados desde el código.


### 6. Despliegue en producción:

   Se deben especificar las variables de entorno para conectar con la base de datos:

      DATABASE_URL=jdbc:mysql://production-database-url:3306/gymapp
      DATABASE_USERNAME=prod_user
      DATABASE_PASSWORD=prod_password

   Usar el siguiente comando para ejecutarlo en producción:

   ```bash
   java -jar target/gymapp.jar --spring.profiles.active=prod
   ```






## Licencia

Este proyecto está licenciado bajo la Licencia MIT.

MIT License

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.