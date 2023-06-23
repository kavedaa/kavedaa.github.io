# Minimal introduction to Scala CLI

There is a relatively new tool out in the the Scala world called [Scala CLI](https://scala-cli.virtuslab.org/) (Scala Command Line Interface). It is useful for scenarios where you need more functionality than what just the Scala compiler provides, but not the full power of a build tool like e.g. SBT.

Here we will give a minimal introduction to Scala CLI and some of it's basic features.

## Download

Download and install Scala CLI from here: https://scala-cli.virtuslab.org/install/

## Hello world

Now, create a `hello.scala` file with the following contents:

```scala
//> using scala 3

@main def main =
  println("Hello world!")
```

Then, in the folder where you saved the file, execute `scala-cli hello.scala`. It should run and print `Hello world!` to the console.

## Directives

Scala CLI uses a special comment syntax for including so-called "directives" directly in the source file:

```scala
//> 
```

In the example above, we used the `scala` directive for specifying which Scala version to use.

## Library dependencies

One of the most useful features of Scala CLI is the ability to specify library dependencies directly in the source file. This is done with the `dep` directive.

Let's create an example where we want to create a PDF file. For this, we might for instance use the [OpenPDF](https://github.com/LibrePDF/OpenPDF) library. This can be added as a dependency to our file like this:


```scala
//> using dep com.github.librepdf:openpdf:1.3.30
```

Full example:

```scala
//> using scala 3
//> using dep com.github.librepdf:openpdf:1.3.30

import java.io.*

import com.lowagie.text.{ Document, Paragraph }
import com.lowagie.text.pdf.PdfWriter

@main def main =
  val document = new Document
  val os = new FileOutputStream(new File("hello.pdf"))
  val writer = PdfWriter.getInstance(document, os)
  document.open()
  document.add(new Paragraph("Hello world!"))
  document.close()
  os.close()
```