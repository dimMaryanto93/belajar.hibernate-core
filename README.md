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
  <property name="hibernate.dialect">org.hibernate.dialect.PostgreSQL95Dialect</property>
  <property name="hibernate.connection.url">jdbc:postgresql://localhost:5432/orm_hibernate</property>
  <property name="hibernate.connection.username">org_hibernate</property>
  <property name="hibernate.connection.password">orm</property>
  <property name="hibernate.connection.pool_size">1</property>
 </session-factory>
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

## Model atau entity

Model merepresentasikan attribut di table pada System Database Management System. Biasanya kita membuat program yang berbasis database baik itu Oracle, Mysql ataupun PostgreSQL kita harus 
mengenal yang namanya perintah SQL. SQL ini merupakan salah satu alat untuk melakukan query ke database.
hal tersebut memang udah kuno??? knapa kuno?? ia lah itu teknologi udah ada sejak kapan tahun
artinya kita masih menggunakan query yang sifatnya terstrukur, tapi terstruktur belum tentu jelek ya!!! hanya udah tidak relevant aja hehehe...

Jaman sekarang, Teknologi yang masih digunakan yaitu ORM atau Object Relational Mapping dan juga NoSQL.
Hibernate ini bisa menggunakan teknologi tersebut anda tinggal milih aja mau pake teknologi yang mana semuannya punya kekurangan dan kelebihan masing2.
Karena saya mau bahas yang paling fundamental jadi kita bahas dulu yang ORM ya. mungkin yang NoSQL lain kesempatan karena saya juga masih mempelajarinya lebih dalam.

Seperti yang telah saya katakan, dengan menggunakan Hibernate ini akan mengurangi kita berinteraksi dengan bahasa SQL.
mengurangi bukan berarti tidak pake ya tetap ja harus tau perintahnya!! karena jujur aja saya sebenarnya tidak terlalu hafal dengan perintah SQL jadi saya lebih suka menggunakan koding Java.


### Orientasi SQL ke Hibernate

Jaman dulu kita koding, hal pertama yang kita buat adalah perancangan ERD atau Normalisasi / Table Relasi, setelah itu baru kita koding ke Javanya.
Sekarang orientasinya kita balik jadi Java yang akan membuatkan perintah SQLnya?? itu semacam generator ya?? ia itu benar.
Tetapi itu tidak jadi patokan kok! kita juga bisa membuat database berserta table-tablenya baru buat mapping di Javanya.

### Perbedaan mendasar dari SQL ke Hibernate (Annotations)

Perbedaanya klo kita menggunakan SQL, kita harus mengurus semuanya mulai dari connection, pooling connectionya, open connection, close connection, trus klo ada error itu harus kita handel manually.
Sedangkan menggunakan Hibernate hal tersebut udah diurusin sama si Hibernate bahkan sampai yang hal lebih detail lagi contohnya tentang constraint `on update cascade on delete cascade` jadi 
setiap kali di update table yang menjadi refenresinya juga akan ikut berubah begitu pula dengan even delete.

### Basic Domain Model dengan Annotation

Membuat basic domain model, pertama kita buat kelas Java misalnya dengan nama `Negara` yang disimpan dalam package `com.hotmail.dimmaryanto.software.belajar` yang isinya seperti berikut:

```java
package com.hotmail.dimmaryanto.software.belajar.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Negara {

    @Id
    @GeneratedValue
    private String id;
    @Column(name = "kode_negara", nullable = false, unique = true, length = 3)
    private String kode;
    @Column(name = "no_area", nullable = false, unique = true, length = 3)
    private Integer area;
    @Column(name = "nama_negara", nullable = false)
    private String nama;
    
    // setter & getter        
}
```

Penjelasan Annotations di atas:

* `@Entity` digunakan untuk mendeteksi bahwa kelas tersebut adalah sebuah entity yang akan di mapping oleh hibernate.
* `@Id` digunakan pada atribute atau variable yang fungsinya untuk menandakan bahwa variable tersebut akan di mapping oleh hibernate sebagai **Primary Key**.
* `@GeneratedValue` digunakan pada atribute atau variabel yang fungsinya untuk menandakan bahwa variable tersebut akan di isi otomatis nilainya sebagai **Sequance** atau **Autoincrement**.
* `@Column` digunakan pada atribute atau variable yang fungsinya untuk menspesifikasikan column di table tersebut contohnya **panjang karakter**, **not null**, dan **unique**.

Kemudian ubah konfigurasi hibernate (`hibernate.cfg.xml`) dengan menambahkan properties seperti berikut:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">
<hibernate-configuration>
    <session-factory>
        
        <!-- properties lainnya....-->
        
        <property name="hibernate.hbm2ddl.auto">update</property>
        <property name="hibernate.show_sql">true</property>
        <!--mapping by xml-->
        <mapping class="com.hotmail.dimmaryanto.software.belajar.model.Negara"></mapping>
    </session-factory>
</hibernate-configuration>
```

Penjelasan property diatas:

* `hibernate.hbm2dd.auto` digunakan untuk strategi generate entity menjadi table di database, yang terdiri dari 4 yaitu create, create-drop, update, validate.
* `hibernate.show_sql` digunakan untuk menampilkan SQL statement sebagai log, setelah di execute oleh hibernate.

Penjelasan tag `mapping`:

tag `mapping` digunakan untuk meregister kelas-kelas Java atau Entity yang akan digunakan oleh hibernate.

Sekarang coba anda compile dan jalankan kelas `App` untuk melihat hasilnya:

```bash
mvn clean compile exec:java -Dexec.mainClass=com.hotmail.dimmaryanto.software.belajar.App
```

hasilnya seperti berikut:

```bash
INFO] --- exec-maven-plugin:1.5.0:java (default-cli) @ orm-hibernate ---
Jan 07, 2017 8:42:59 AM org.hibernate.Version logVersion
INFO: HHH000412: Hibernate Core {5.2.6.Final}
Jan 07, 2017 8:42:59 AM org.hibernate.cfg.Environment <clinit>
INFO: HHH000206: hibernate.properties not found
Jan 07, 2017 8:42:59 AM org.hibernate.annotations.common.reflection.java.JavaReflectionManager <clinit>
INFO: HCANN000001: Hibernate Commons Annotations {5.0.1.Final}
Jan 07, 2017 8:42:59 AM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl configure
WARN: HHH10001002: Using Hibernate built-in connection pool (not for production use!)
Jan 07, 2017 8:42:59 AM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001005: using driver [org.postgresql.Driver] at URL [jdbc:postgresql://localhost:5432/orm_hibernate]
Jan 07, 2017 8:42:59 AM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001001: Connection properties: {user=postgres, password=****}
Jan 07, 2017 8:42:59 AM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl buildCreator
INFO: HHH10001003: Autocommit mode: false
Jan 07, 2017 8:42:59 AM org.hibernate.engine.jdbc.connections.internal.PooledConnections <init>
INFO: HHH000115: Hibernate connection pool size: 1 (min=1)
Jan 07, 2017 8:43:00 AM org.hibernate.dialect.Dialect <init>
INFO: HHH000400: Using dialect: org.hibernate.dialect.PostgreSQL95Dialect
Jan 07, 2017 8:43:00 AM org.hibernate.engine.jdbc.env.internal.LobCreatorBuilderImpl useContextualLobCreation
INFO: HHH000424: Disabling contextual LOB creation as createClob() method threw error : java.lang.reflect.InvocationTargetException
Jan 07, 2017 8:43:00 AM org.hibernate.type.BasicTypeRegistry register
INFO: HHH000270: Type registration [java.util.UUID] overrides previous : org.hibernate.type.UUIDBinaryType@6505d21a
Jan 07, 2017 8:43:00 AM org.hibernate.resource.transaction.backend.jdbc.internal.DdlTransactionIsolatorNonJtaImpl getIsolatedConnection
INFO: HHH10001501: Connection obtained from JdbcConnectionAccess [org.hibernate.engine.jdbc.env.internal.JdbcEnvironmentInitiator$ConnectionProviderJdbcConnectionAccess@2f5396ba] for (non-JTA) DDL execution was not in auto-commit mode; the Connection 'local transaction' will be committed and the Connection will be set into auto-commit mode.
Hibernate: create table Negara (id varchar(255) not null, no_area int4 not null, kode_negara varchar(3) not null, nama_negara varchar(255) not null, primary key (id))
Hibernate: alter table Negara drop constraint if exists UK_hdx3qooqyjm09bpq36fsw5lto
Jan 07, 2017 8:43:01 AM org.hibernate.engine.jdbc.spi.SqlExceptionHelper$StandardWarningHandler logWarning
WARN: SQL Warning Code: 0, SQLState: 00000
Jan 07, 2017 8:43:01 AM org.hibernate.engine.jdbc.spi.SqlExceptionHelper$StandardWarningHandler logWarning
WARN: constraint "uk_hdx3qooqyjm09bpq36fsw5lto" of relation "negara" does not exist, skipping
Hibernate: alter table Negara add constraint UK_hdx3qooqyjm09bpq36fsw5lto unique (no_area)
Hibernate: alter table Negara drop constraint if exists UK_9b5ag0t3hpbxthvm792e9bxih
Jan 07, 2017 8:43:01 AM org.hibernate.engine.jdbc.spi.SqlExceptionHelper$StandardWarningHandler logWarning
WARN: SQL Warning Code: 0, SQLState: 00000
Jan 07, 2017 8:43:01 AM org.hibernate.engine.jdbc.spi.SqlExceptionHelper$StandardWarningHandler logWarning
WARN: constraint "uk_9b5ag0t3hpbxthvm792e9bxih" of relation "negara" does not exist, skipping
Hibernate: alter table Negara add constraint UK_9b5ag0t3hpbxthvm792e9bxih unique (kode_negara)
Jan 07, 2017 8:43:01 AM org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl stop
INFO: HHH10001008: Cleaning up connection pool [jdbc:postgresql://localhost:5432/orm_hibernate]
[INFO] ------------------------------------------------------------------------
```

Nah coba perhatikan perintah berikut:

```sql
Hibernate: 
  create table Negara (
    id varchar(255) not null, 
    no_area int4 not null, 
    kode_negara varchar(3) not null, 
    nama_negara varchar(255) not null, 
    primary key (id)
  )
Hibernate: alter table Negara drop constraint if exists UK_hdx3qooqyjm09bpq36fsw5lto
```

Itu adalah hasil generate yang dilakukan oleh hibernate dari kelas `Negara` yang ada di package `com.hotmail.dimmaryanto.software.belajar.model`.