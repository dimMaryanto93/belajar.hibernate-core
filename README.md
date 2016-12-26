## Hibernate ORM

Hibernate ORM, jaman sekarang hibernate adalah teknologi yang wajib diketahui baik itu secara konsep maupun secara fisikal (Koding). Pertanyaanya buat apa? khan bisa pake JDBC biasa?

1. Yang pertama kenapa menggunakan Hibernate? jawabanya adalah Strukturnya lebih mudah, tidak terlalu banyak menggunakan `try-catch`, Cara berpilirnya lebih ke Object Oriented bukan ke struktur data database.

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

* Update pom.xml

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
