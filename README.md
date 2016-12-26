## Hibernate ORM

Hibernate ORM, jaman sekarang hibernate adalah teknologi yang wajib diketahui baik itu secara konsep maupun secara fisikal (Koding). Pertanyaanya buat apa? khan bisa pake JDBC biasa?

1. Yang pertama kenapa menggunakan Hibernate? jawabanya adalah Strukturnya lebih mudah, tidak terlalu banyak menggunakan `try-catch`, Cara berpikirnya lebih ke Object Oriented bukan ke struktur data database.

2. Yang kedua khan bisa menggunakan JDBC biasa? jawabanya adalah Hibernate juga mengimplementasikan JDBC jadi klo belajar juga gak akan ada salahnya toh konsepnya hampir sama (menurutku ya...).

### Persiapan

* Install Java 1.8
* Install Apache Maven
* Install Database (Mysql, PostgreSql, Oracle, atau lainnya)

Karena di laptop saya lebih sering menggunakan PostgreSql jadi saya pakai PostgreSQL aja ya...

* Membuat Project Java dari Maven

Membuat project dari maven repository, karena kita hanya membuat beberapa penggalan kode di console jadi kita pilih semuanya yang default aja. seperti berikut:

```bash
mvn archetype:generate -DartifactId=orm-hibernate -DgroupId=com.hotmail.dimmaryanto.software.belajar -Dversion=1.0 -DarchetypeCatalog='internal' -DarchetypeArtifactId=maven-archetype-quickstart
```

* Setup `pom.xml`

Ubah dependency artifactId=junit menjadi versi terbaru klo di saya pake versi 4.12

```xml
<dependency>
	<groupId>junit</groupId>
	<artifactId>junit</artifactId>
	<version>4.12</version>
	<scope>test</scope>
</dependency>
```

Kemudian tambahkan plugin dengan artifactId=maven-compiler-plugin dari groupId=org.apache.maven.plugins seperti berikut

```xml
<project>
	<!-- ... disini tag lainnya -->
	<!-- ... disini tag dependency -->
	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.5.1</version>
				<configuration>
					<source>1.8</source>
					<target>1.8</target>
				</configuration>
			</plugin>
		</plugins>
	</build>
</project>
```

Lalu setelah kita ubah konfigurasi JDK dari `1.5` ke `1.8` tahap salanjutnya adalah menambahkan dependecy hibernate yaitu dengan artifactId=hibernate-core dari groupId=org.hibernate seperti berikut:

```xml
<dependency>
   <groupId>org.hibernate</groupId>
   <artifactId>hibernate-core</artifactId>
   <version>5.2.6.Final</version>
</dependency>
```

Udah beres??? eitsss ada yang ketinggalan JDBC untuk postgreSQLnya belum ok tambahkan dulu ya seperti berikut:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>9.4.1212.jre7</version>
</dependency>
```

### Konfigurasi Hibernate

Konfigurasi Hibernate pada dasarnya ada 2 jenis konfigurasi yaitu dengan Source Code java atau saya lebih sering sebut Java Config dan XML (hibernate.cfg.xml). Jaman sekarang Konfigurasi dengan XML udah sangat jarang karena mengikuti style Java yang berbau _annotation_ (`@Annotations`). Tpi gak apa karena kita mau niatnya belajar jadi Ya ikutin aja ya... karena klo kita menggunakan XML juga gak ada ruginya kok toh XML juga lebih mudah dibaca dibandingkan Java Config.

Tahap selanjutnya kita harus buat file dengan nama `hibernate.cfg.xml` dalam source code kita. Karena kita menggunakan Apache Maven jadi kita buat dalam folder `src/main/resources` klo belum ada silahkan buat dulu foldernya setelah itu buat filenya yaitu `hibernate.cfg.xml`

Jika sudah tambahkan tag seperti berikut ke file tersebut:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
		"-//Hibernate/Hibernate Configuration DTD 3.0//EN"
		"http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
</hibernate-configuration>
```

Tags `<hibernate-configuraion>` adalah container untuk kita menyimpan configurasi hibernate seperti connection ke database, mapping entity, configurasi lainya seperti ketika hibernate di execute maka dia menampilkan sqlnya di console, generate otomatis table dari entity dengan schema `update` atau `create` atau `create-drop` dan `validate`.

Nah sebelum masuk ke konfigurasi lebih lanjut kita buat dulu databasenya, contohnya nama databasenya `orm_hibernate` kemudian saya juga mau membuat user dengan nama `org_hibernate` password `orm` jadi kurang lebih seperti berikut:

```bash
## login dulu pke psql
dimmaryanto93@ASPIRE-e14:~$ psql -h localhost -U postgres 
psql (9.5.5)
SSL connection (protocol: TLSv1.2, cipher: ECDHE-RSA-AES256-GCM-SHA384, bits: 256, compression: off)
Type "help" for help.

## bikin user dan password
postgres=# create user org_hibernate with superuser login password 'orm';
CREATE ROLE

## bikin databasenya
postgres=# create database orm_hibernate with owner org_hibernate;
CREATE DATABASE
```

Setelah setup databasenya selesai, kita balik lagi ke Konfigurasi Hibernate (hibernate.cfg.xml) tambahkan seperti berikut sesuai koneksi database yang kita buat tadi:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
                                         "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
 <session-factory>
  <property name="hibernate.connection.driver_class">org.postgresql.Driver</property>
  <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQLDialect</property>
  <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/orm_hibernate</property>
  <property name="hibernate.connection.username">org_hibernate</property>
  <property name="hibernate.connection.password">orm</property>
  <property name="hibernate.connection.pool_size">1</property>
 </session-factory
</hibernate-configuration>
```

Setelah itu kita buat kelas Java `HibernateFactory` dalam package `com.hotmail.dimmaryanto.software.belajar` lalu kita buat configurasi SessionFactory. seperti berikut:

```java
package com.hotmail.dimmaryanto.software.belajar;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateFactory {
	
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
				.configure() // configures settings from hibernate.cfg.xml
				.build();
		try {
			sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
		}catch (Exception e) {
			// The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
			// so destroy it manually.
			StandardServiceRegistryBuilder.destroy( registry );
		}
		return sessionFactory;
	}
	
	public static void main(String[] args){
		HibernateFactory factory = new HibernateFactory();
		SessionFactory sessionFactory = factory.getSessionFactory();
		Session session = sessionFactory.openSession();
		session.close();
		sessionFactory.close();
	}
}
```

Setelah itu coba anda jalankan dengan perintah maven seperti berikut:

```bash
mvn clean compile exec:java -Dexec.mainClass=com.hotmail.dimmaryanto.software.belajar.HibernateFactory
```

Maka outputnya seperti berikut:

```bash
dimmaryanto93@ASPIRE-e14:~/Temp/orm-hibernate$ mvn clean compile exec:java -Dexec.mainClass=com.hotmail.dimmaryanto.software.belajar.HibernateFactory
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building Hibernate - Object Relational Mapping 1.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ orm-hibernate ---
[INFO] Deleting /home/dimmaryanto93/Temp/orm-hibernate/target
[INFO] 
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ orm-hibernate ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 1 resource
[INFO] 
[INFO] --- maven-compiler-plugin:3.5.1:compile (default-compile) @ orm-hibernate ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 2 source files to /home/dimmaryanto93/Temp/orm-hibernate/target/classes
[INFO] 
[INFO] --- exec-maven-plugin:1.5.0:java (default-cli) @ orm-hibernate ---
Dec 26, 2016 10:59:56 PM org.hibernate.Version logVersion
INFO: HHH000412: Hibernate Core {5.2.6.Final}
Dec 26, 2016 10:59:56 PM org.hibernate.cfg.Environment <clinit>
INFO: HHH000206: hibernate.properties not found
Dec 26, 2016 10:59:56 PM org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
Dec 26, 2016 10:59:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl configure
WARN: HHH10001002: Using Hibernate built-in connection pool (not for production use!)
Dec 26, 2016 10:59:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001005: using driver [org.postgresql.Driver] at URL [jdbc:postgresql://localhost:5432/orm_hibernate]
Dec 26, 2016 10:59:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001001: Connection properties: {user=org_hibernate, password=****}
Dec 26, 2016 10:59:56 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001003: Autocommit mode: false
Dec 26, 2016 10:59:56 PM org.hibernate.engine.jdbc.connections.internal.PooledConnections <init>
INFO: HHH000115: Hibernate connection pool size: 1 (min=1)
Dec 26, 2016 10:59:56 PM org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.PostgreSQLDialect
Dec 26, 2016 10:59:57 PM org.hibernate.engine.jdbc.env.internal.LobCreatorBuilderImpl useContextualLobCreation
INFO: HHH000424: Disabling contextual LOB creation as createClob() method threw error : java.lang.reflect.InvocationTargetException
Dec 26, 2016 10:59:57 PM org.hibernate.type.BasicTypeRegistry register
INFO: HHH000270: Type registration [java.util.UUID] overrides previous : org.hibernate.type.UUIDBinaryType@180e0d7e
Dec 26, 2016 10:59:57 PM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl stop
INFO: HHH10001008: Cleaning up connection pool [jdbc:postgresql://localhost:5432/orm_hibernate]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 5.162 s
[INFO] Finished at: 2016-12-26T22:59:57+07:00
[INFO] Final Memory: 23M/226M
[INFO] ------------------------------------------------------------------------
```

Itu tandanya configurasinya udah benar dan udah terhubung dengan database `jdbc:postgresql://localhost:5432/orm_hibernate`
