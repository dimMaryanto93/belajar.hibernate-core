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
