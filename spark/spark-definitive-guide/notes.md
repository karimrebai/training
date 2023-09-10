# Part I. Gentle overview

## Chapter 2. A gentle introduction to Spark

### Partitions

- If you have one partition, Spark will have a parallelism of only one,
  even if you have thousands of executors. If you have many partitions but only one executor,
  Spark will still have a parallelism of only one because there is only one computation resource.

### Transformations

- In Spark, the core data structures are immutable.

- 2 types of transformations:
    - narrow: each input partition will contribute to only one output partition.
    - wide: input partitions contribute to many output partitions

### Lazy Evalution

- Lazy evaulation means that Spark will wait until the very last moment to execute the graph of computation
  instructions.

- By waiting until the last minute to execute the code, Spark compiles this plan from your raw DataFrame transformations
  to a streamlined physical plan that will run as efficiently as possible across the cluster.
  This provides immense benefits because Spark can optimize the entire data flow from end to end.
  Ex: predicate pushdown on DataFrames.

- By default, when we perform a shuffle, Spark outputs 200 shuffle partitions. Let’s set this value to 5 to reduce the
  number of the output partitions from the shuffle:
  spark.conf.set("spark.sql.shuffle.partitions", "5")

### Actions

- An action instructs Spark to compute a result from a series of transformations.

- 3 kinds of actions:
    - Actions to view data in the console
    - Actions to collect data to native objects in the respective language
    - Actions to write to output data sources

## Chapter 3. A tour of Spark's toolset

### Running Production Applications

- spark-submit does one thing: it lets you send your application code to a cluster and launch it to execute there.

- Upon submission, the application will run until it exits (completes the task) or encounters an error.

- By changing the master argument of spark-submit, we can also submit the same application to a cluster running Spark’s
  standalone cluster manager, Mesos or YARN.

### Datasets: Type-Safe Structured APIs

- Used to write statically typed code in Java and Scala

- Not available in Python and R because those languages are dynamically typed.

- The Dataset API gives users the ability to assign a Java/Scala class to the records within a DataFrame and manipulate
  it as a collection of typed objects, similar to a Java ArrayList or Scala Seq

- The APIs available on Datasets are type-safe.
  For example, a Dataset[Person] will be guaranteed to contain objects of class Person

### Structured Streaming

- With Structured Streaming, you can take the same operations that you perform in batch mode using Spark’s structured
  APIs and run them in a streaming fashion. This can reduce latency and allow for incremental processing.

### Machine learning and Advanced analytics

- Perform large-scale machine learning with a built-in library of machine learning algorithms called MLlib.
  MLlib allows for preprocessing, munging, training of models, and making predictions at scale on data.

- k-means is a clustering algorithm in which “k” centers are randomly assigned within the data. The
  points closest to that point are then “assigned” to a class and the center of the assigned points is
  computed. This center point is called the centroid. We then label the points closest to that centroid, to
  the centroid’s class, and shift the centroid to the new center of that cluster of points. We repeat this
  process for a finite set of iterations or until convergence (our center points stop changing).

### Lower level APIs

- There are some things that you might use RDDs for, especially when you’re reading
  or manipulating raw data, but for the most part you should stick to the Structured APIs. RDDs
  are lower level than DataFrames because they reveal physical execution characteristics (like
  partitions) to end users.

### Summary

Spark is a distributed programming model in which the user specifies transformations. Multiple
transformations build up a directed acyclic graph of instructions. An action begins the process of
executing that graph of instructions, as a single job, by breaking it down into stages and tasks to
execute across the cluster. The logical structures that we manipulate with transformations and actions
are DataFrames and Datasets. To create a new DataFrame or Dataset, you call a transformation. To
start computation or convert to native language types, you call an action.

# Part II. Structured APIs

## Chapter 4. Structured API overview

The 3 kinds of structured APIs are:

- Datasets
- DataFrames
- SQL tables and views

### Overview of Structured API Execution

- Overview of the steps:

1. Write DataFrame/Dataset/SQL Code.
2. If valid code, Spark converts this to a Logical Plan.
3. Spark transforms this Logical Plan to a Physical Plan, checking for optimizations along the way.
4. Spark then executes this Physical Plan (RDD manipulations) on the cluster.

## Chapter 5. Basic Structured Operations

### Schemas

- A schema defines the column names and types of a DataFrame. We can either let a data source
  define the schema (called schema-on-read) or we can define it explicitly ourselves.

- A schema is a StructType made up of a number of fields, StructFields, that have a name, a type and a boolean
  for nullable.

/!\ When using Spark for production => define your schemas manually

### Columns and expressions

- A column might or might not exist in our DataFrames. Columns are not resolved until we compare the column names with
  those we are maintaining in the catalog. Column and table resolution happens in the analyzer phase.

- An expression is a function that takes as input one or more column names, resolves them, and then potentially
  applies more expressions to create a single value for each record in the dataset.

- Columns represent a subset of expression functionality.

- Columns and transformations of those columns compile to the same logical plan as parsed expressions.

- SQL expressions and DataFrame code compile to the same underlying logical tree prior to execution.

### Records and rows

- Each row in a DataFrame is a single record. Spark represents this record as an object of type Row.
  Spark manipulates Row objects using column expressions in order to produce usable values.
  Row objects internally represent arrays of bytes.

- To access to data in rows you just specify the position that you would like. In Scala or Java, you must either use the
  helper methods or explicitly coerce the values.

### Dataframe transformations

#### Creating dataframes

- We can create DataFrames from raw data sources:

```scala
val df = spark.read.format("json").load("/data/flight-data/json/2015-summary.json")
```

- We can also create DataFrames on the fly by taking a set of rows and converting them to a DataFrame:

```scala
val myManualSchema = new StructType(Array(
  new StructField("some", StringType, true),
  new StructField("col", StringType, true),
  new StructField("names", LongType, false)))
val myRows = Seq(Row("Hello", null, 1L))
val myRDD = spark.sparkContext.parallelize(myRows)
val myDf = spark.createDataFrame(myRDD, myManualSchema)
```

- In Scala, we can also take advantage of Spark’s implicits in the console (and if you import them in
  your JAR code) by running toDF on a Seq type. This does not play well with null types, so it’s not
  necessarily recommended for production use cases.

#### select and selectExpr

```scala
df.select("DEST_COUNTRY_NAME", "ORIGIN_COUNTRY_NAME")

df.select(
  df.col("DEST_COUNTRY_NAME"),
  col("DEST_COUNTRY_NAME"),
  column("DEST_COUNTRY_NAME"),
  'DEST_COUNTRY_NAME,
  $"DEST_COUNTRY_NAME",
  expr("DEST_COUNTRY_NAME"))
```

expr is the most flexible reference that we can use. It can refer to a plain column or a string manipulation of a
column. To illustrate, let’s change the column name, and then change it back by using the AS keyword and then the alia
method on the column:

```scala
df.select(expr("DEST_COUNTRY_NAME as destination").alias("DEST_COUNTRY_NAME"))

df.selectExpr("DEST_COUNTRY_NAME as newColumnName", "DEST_COUNTRY_NAME")
```

This opens up the true power of Spark. We can treat selectExpr as a simple way to build up complex expressions that
create new DataFrames. In fact, we can add any valid non-aggregating SQL statement, and as long as the columns resolve,
it will be valid!

```scala
df.selectExpr(
  "*", // include all original columns
  "(DEST_COUNTRY_NAME = ORIGIN_COUNTRY_NAME) as withinCountry")
```

With select expression, we can also specify aggregations over the entire DataFrame by taking advantage of the functions
that we have. These look just like what we have been showing so far:

```scala
df.selectExpr("avg(count)", "count(distinct(DEST_COUNTRY_NAME))")
```

#### Literals

Sometimes, we need to pass explicit values into Spark that are just a value (rather than a new column). This might be a
constant value or something we’ll need to compare to later on. The way we do this is through literal:

```scala
df.select(expr("*"), lit(1).as("One"))
```

This will come up when you might need to check whether a value is greater than some constant or other programmatically
created variable.

#### Adding columns

```scala
df.withColumn("withinCountry", expr("ORIGIN_COUNTRY_NAME == DEST_COUNTRY_NAME"))

df.withColumn("Destination", expr("DEST_COUNTRY_NAME"))
```

#### Renaming columns

```scala
df.withColumnRenamed("DEST_COUNTRY_NAME", "dest")
```

#### Reserved Characters and keywords

One thing that you might come across is reserved characters like spaces or dashes in column names. Handling these means
escaping column names appropriately. In Spark, we do this by using backtick (`) characters:

```scala
dfWithLongColName.selectExpr(
  "`This Long Column-Name`",
  "`This Long Column-Name` as `new col`") \

dfWithLongColName.select(col("This Long Column-Name")).columns
```

#### Case Sensitivity

By default, Spark is case insensitive; however, you can make Spark case sensitive by setting the configuration:

```sparksql
set spark.sql.caseSensitive true
```

#### Removing columns

```scala
dfWithLongColName.drop("ORIGIN_COUNTRY_NAME", "DEST_COUNTRY_NAME")
```

#### Changing a Column’s Type (cast)

```scala
df.withColumn("count2", col("count").cast("long"))
```

#### Filtering rows

```scala
df.filter(col("count") < 2)
// Is same as:
df.where("count < 2")
```

Instinctually, you might want to put multiple filters into the same expression. Although this is possible, it is not
always useful, because Spark automatically performs all filtering operations at the same time regardless of the filter
ordering. This means that if you want to specify multiple AND filters, just chain them sequentially and let Spark handle
the rest:

```scala
df.where(col("count") < 2).where(col("ORIGIN_COUNTRY_NAME") =!= "Croatia")
```

#### Getting Unique Rows

```scala
df.select("ORIGIN_COUNTRY_NAME", "DEST_COUNTRY_NAME").distinct().count()
```

#### Concatenating and Appending Rows (Union)

To union two DataFrames, you must be sure that they have the same schema and number of columns; otherwise, the union
will fail:

```scala
val schema = df.schema
val newRows = Seq(
  Row("New Country", "Other Country", 5L),
  Row("New Country 2", "Other Country 3", 1L)
)
val parallelizedRows = spark.sparkContext.parallelize(newRows)
val newDF = spark.createDataFrame(parallelizedRows, schema)
df.union(newDF)
  .where("count = 1")
  .where($"ORIGIN_COUNTRY_NAME" =!= "United States")
  .show()
```

As expected, you’ll need to use this new DataFrame reference in order to refer to the DataFrame with the newly appended
rows. A common way to do this is to make the DataFrame into a view or register it as a table so that you can reference
it more dynamically in your code.

#### Sorting Rows

```scala
df.sort("count").show(5)
df.orderBy("count", "DEST_COUNTRY_NAME").show(5)
df.orderBy(col("count"), col("DEST_COUNTRY_NAME")).show(5)

import org.apache.spark.sql.functions.{desc, asc}

df.orderBy(expr("count desc")).show(2)
df.orderBy(desc("count"), asc("DEST_COUNTRY_NAME")).show(2)
```

Tip: use asc_nulls_first, desc_nulls_first, asc_nulls_last, or desc_nulls_last to specify where you would like your null
values to appear in an ordered DataFrame.

For optimization purposes, it’s sometimes advisable to sort within each partition before another set of transformations.
You can use the sortWithinPartitions method to do this:

```scala
spark.read.format("json").load("/data/flight-data/json/*-summary.json")
  .sortWithinPartitions("count")
```

#### Repartition and Coalesce

Another important optimization opportunity is to partition the data according to some frequently filtered columns, which
control the physical layout of data across the cluster including the partitioning scheme and the number of partitions.

Repartition will incur a full shuffle of the data, regardless of whether one is necessary. This means that you
should typically only repartition when the future number of partitions is greater than your current number of
partitions or when you are looking to partition by a set of columns:

```scala
df.rdd.getNumPartitions // 1
df.repartition(5)
```

If you know that you’re going to be filtering by a certain column often, it can be worth repartitioning based on
that column:

```scala
df.repartition(5, col("DEST_COUNTRY_NAME"))
```

Coalesce, on the other hand, will not incur a full shuffle and will try to combine partitions. This operation will
shuffle your data into five partitions based on the destination country name, and then coalesce them (without a
full shuffle):

```scala
df.repartition(5, col("DEST_COUNTRY_NAME")).coalesce(2)
```

#### Collecting Rows to the Driver

Spark maintains the state of the cluster in the driver. There are times when you’ll want to collect some of your
data to the driver in order to manipulate it on your local machine:

```scala
df.take(5) // take selects the first 5 rows
df.show(5, false) // this prints it out nicely
df.collect() // gets all data from the entire DataFrame

collectDF.toLocalIterator() // collects partitions to the driver as an iterator, allows you to iterate over the entire
// dataset partition-by-partition in a serial manner
```

**/!\ WARNING** : If you have a large dataset and call collect, you can crash the driver. If you use toLocalIterator
and have very
large partitions, you can easily crash the driver node and lose the state of your application. This is also expensive
because we can operate on a one-by-one basis, instead of running computation in parallel.

## Chapter 6. Working with Different Types of Data

### Converting Spark Types

The lit function converts a type in another language to its corresponding Spark representation:

```scala
df.select(lit(5), lit("five"), lit(5.0))
```

### Working with Booleans

```scala
df.where(col("InvoiceNo").equalTo(536365))
df.where(col("InvoiceNo") === 536365)
df.where("InvoiceNo = 536365") // Cleanest one by specifying the predicate as an expression in a string

df.where("InvoiceNo <> 536365")
```

Although you can specify your statements explicitly by using and if you like, they’re often easier to understand and
to read if you specify them serially. or statements need to be specified in the same statement:

```scala
val priceFilter = col("UnitPrice") > 600
val descFilter = col("Description").contains("POSTAGE")
df.where(col("StockCode").isin("DOT")).where(priceFilter.or(descFilter))
```

Boolean expressions are not just reserved to filters. To filter a DataFrame, you can also just specify a Boolean column:

```scala
val DOTCodeFilter = col("StockCode") === "DOT"
val priceFilter = col("UnitPrice") > 600
val descFilter = col("Description").contains("POSTAGE")
df.withColumn("isExpensive", DOTCodeFilter.and(priceFilter.or(descFilter))).where("isExpensive")
```

It’s often easier to just express filters as SQL statements than using the programmatic DataFrame interface:

```scala
df.withColumn("isExpensive", not(col("UnitPrice").leq(250))).filter("isExpensive")
// is equivalent to:
df.withColumn("isExpensive", expr("NOT UnitPrice <= 250")).filter("isExpensive")
```

### Working with Strings

When we convert a list of values into a set of arguments and pass them into a function, we use a language feature
called varargs. Using this feature, we can effectively unravel an array of arbitrary length and pass it as arguments
to a function. This, coupled with select makes it possible for us to create arbitrary numbers of columns dynamically:

```scala
val simpleColors = Seq("black", "white", "red", "green", "blue")
val selectedColumns = simpleColors.map(color => {
  col("Description").contains(color.toUpperCase).alias(s"is_$color")
}) :+ expr("*") // could also append this value
df.select(selectedColumns: _*).where(col("is_white").or(col("is_red")))
  .select("Description").show(3, false)
```

### Working with Dates and Timestamps

A common “gotcha” is that Spark’s TimestampType class supports only second-level precision, which means that if
you’re going to be working with milliseconds or microseconds, you’ll need to work around this problem by potentially
operating on them as longs. Any more precision when coercing to a TimestampType will be removed.

```scala
val dateDF = spark.range(10)
  .withColumn("today", current_date())
  .withColumn("now", current_timestamp())

dateDF.select(date_sub(col("today"), 5), date_add(col("today"), 5)).show(1)

dateDF.withColumn("week_ago", date_sub(col("today"), 7)).select(datediff(col("week_ago"), col("today"))).show(1)

dateDF.select(
  to_date(lit("2016-01-01")).alias("start"),
  to_date(lit("2017-05-22")).alias("end"))
  .select(months_between(col("start"), col("end"))).show(1)
```

/!\ Spark will not throw an error if it cannot parse the date; rather, it will just return null. This can
be a bit tricky in larger pipelines because you might be expecting your data in one format and getting it in another.

/!\ Implicit type casting is an easy way to shoot yourself in the foot, especially when dealing with null values or
dates in different timezones or formats. We recommend that you parse them explicitly instead of relying on implicit
conversions.


### Working with Nulls in Data

As a best practice, you should always use nulls to represent missing or empty data in your DataFrames. Spark can 
optimize working with null values more than it can if you use empty strings or other values.

#### Coalesce
Spark includes a function to allow you to select the first non-null value from a set of columns by using coalesce:
```scala
df.select(coalesce(col("Description"), col("CustomerId"))).show()
```

#### ifnull, nullIf, nvl, and nvl2
```sql
SELECT
ifnull(null, 'return_value'), -- return_value
nullif('value', 'value'), -- null
nvl(null, 'return_value'), -- return_value
nvl2('not_null', 'return_value', "else_value") -- return_value
```

#### drop
The simplest function is drop, which removes rows that contain nulls:
```scala
df.na.drop()
df.na.drop("all", Seq("StockCode", "InvoiceNo"))
```

#### fill
Using the fill function, you can fill one or more columns with a set of values:
```scala
df.na.fill("All Null values become this string")

df.na.fill(5, Seq("StockCode", "InvoiceNo"))

val fillColValues = Map("StockCode" -> 5, "Description" -> "No Value")
df.na.fill(fillColValues)
```

#### Replace
Replace all values in a certain column according to their current value. The only requirement is that this value be 
the same type as the original value:
```scala
df.na.replace("Description", Map("" -> "UNKNOWN"))
```

### Working with Complex Types

#### Structs

You can think of structs as Dataframes within Dataframes

```scala
val complexDF = df.select(struct("Description", "InvoiceNo").alias("complex"))
complexDF.createOrReplaceTempView("complexDF")

complexDF.select("complex.Description")
```

#### Arrays

```scala
// split
df.select(split(col("Description"), " ").alias("array_col"))
        .selectExpr("array_col[0]").show(2)

// length
df.select(size(split(col("Description"), " "))).show(2)

// contains
df.select(array_contains(split(col("Description"), " "), "WHITE")).show(2)
```

The explode function takes a column that consists of arrays and creates one row (with the rest of the values 
duplicated) per value in the array:
```scala
df.withColumn("splitted", split(col("Description"), " "))
.withColumn("exploded", explode(col("splitted")))
.select("Description", "InvoiceNo", "exploded").show(2)
```

#### Maps

```scala
df.select(map(col("Description"), col("InvoiceNo")).alias("complex_map")).show(2)

df.select(map(col("Description"), col("InvoiceNo")).alias("complex_map"))
        .selectExpr("complex_map['WHITE METAL LANTERN']").show(2)

df.select(map(col("Description"), col("InvoiceNo")).alias("complex_map"))
        .selectExpr("explode(complex_map)").show(2) 
```

## User-Defined Functions

One of the most powerful things that you can do in Spark is define your own functions. These user-defined functions 
(UDFs) make it possible for you to write your own custom transformations using Python or Scala and even use external 
libraries.

```scala
import org.apache.spark.sql.functions.udf
val power3udf = udf(power3(_:Double):Double)

udfExampleDF.select(power3udf(col("num"))).show()

spark.udf.register("power3", power3(_:Double):Double)
udfExampleDF.selectExpr("power3(num)").show(2)
```
It is important to note that specifying the return type is not necessary, but it is a best practice.


## Chapter 7. Aggregations

Let’s begin by reading in our data on purchases, repartitioning the data to have far fewer partitions (because we 
know it’s a small volume of data stored in a lot of small files), and caching the results for rapid access:

```scala
val df = spark.read.format("csv")
.option("header", "true")
.option("inferSchema", "true")
.load("/data/retail-data/all/*.csv")
.coalesce(5)
df.cache()
df.createOrReplaceTempView("dfTable")
```

You can use count to get an idea of the total size of your dataset but another common pattern is to use it to cache 
an entire DataFrame in memory, just like we did in this example.

### Aggregation functions

- #### count / countDistinct / approx_count_distinct
- #### first and last
- #### min and max
- #### sum / sumDistinct
- #### avg
```scala
df.select(
count("Quantity").alias("total_transactions"),
sum("Quantity").alias("total_purchases"),
avg("Quantity").alias("avg_purchases"),
expr("mean(Quantity)").alias("mean_purchases"))
.selectExpr(
"total_purchases/total_transactions",
"avg_purchases",
"mean_purchases").show()
```
- #### Variance and Standard Deviation

  Calculating the mean naturally brings up questions about the variance and standard deviation.
  These are both measures of the spread of the data around the mean:
```scala
df.select(var_pop("Quantity"), var_samp("Quantity"),
stddev_pop("Quantity"), stddev_samp("Quantity")).show()
```

- #### skewness and kurtosis

  Skewness and kurtosis are both measurements of extreme points in your data. Skewness
  measures the asymmetry of the values in your data around the mean, whereas kurtosis is a
  measure of the tail of data

- #### Aggregating to Complex Types

  In Spark, you can perform aggregations not just of numerical values using formulas, you can also
  perform them on complex types. For example, we can collect a list of values present in a given
  column or only the unique values by collecting to a set.
  You can use this to carry out some more programmatic access later on in the pipeline or pass the
  entire collection in a user-defined function (UDF):
```scala
import org.apache.spark.sql.functions.{collect_set, collect_list}
df.agg(collect_set("Country"), collect_list("Country")).show()
```

### Grouping

We do this grouping in two phases. First we specify the column(s) on which we would like to
group, and then we specify the aggregation(s). The first step returns a
RelationalGroupedDataset, and the second step returns a DataFrame:
```scala
df.groupBy("InvoiceNo", "CustomerId").count().show()
```

#### Grouping with Expressions

As we saw earlier, counting is a bit of a special case because it exists as a method. For this,
usually we prefer to use the count function. Rather than passing that function as an expression
into a select statement, we specify it as within agg:
```scala
df.groupBy("InvoiceNo").agg(
count("Quantity").alias("quan"),
expr("count(Quantity)")).show()
```

#### Grouping with Maps
Sometimes, it can be easier to specify your transformations as a series of Maps for which the key
is the column, and the value is the aggregation function (as a string) that you would like to
perform. You can reuse multiple column names if you specify them inline, as well:
```scala
df.groupBy("InvoiceNo").agg("Quantity"->"avg", "Quantity"->"stddev_pop").show()
```


### Window Functions

You can also use window functions to carry out some unique aggregations by either computing some aggregation on a 
specific “window” of data, which you define by using a reference to the current data. This window specification 
determines which rows will be passed in to this function.
Now this is a bit abstract and probably similar to a standard group-by, so let’s differentiate them a bit more.
A group-by takes data, and every row can go only into one grouping. A window function calculates a return value for 
every input row of a table based on a group of rows, called a frame. Each row can fall into one or more frames. A 
common use case is to take a look at a rolling average of some value for which each row represents one day. If you 
were to do this, each row would end up in seven different frames. We cover defining frames a little later, but for your
reference, Spark supports three kinds of window functions: ranking functions, analytic functions, and aggregate 
functions.
```scala
val dfWithDate = df.withColumn("date", to_date(col("InvoiceDate"), "MM/d/yyyy H:mm"))
dfWithDate.createOrReplaceTempView("dfWithDate")
```

## Chapter 8. Joins

### Join types

- Inner joins (keep rows with keys that exist in the left and right datasets)
- Outer joins (keep rows with keys in either the left or right datasets)
- Left outer joins (keep rows with keys in the left dataset)
- Right outer joins (keep rows with keys in the right dataset)
- Left semi joins (keep the rows in the left, and only the left, dataset where the key appears in the right dataset)
- Left anti joins (keep the rows in the left, and only the left, dataset where they do not appear in the right 
  dataset)
- Natural joins (perform a join by implicitly matching the columns between the two datasets with the same names)
- Cross (or Cartesian) joins (match every row in the left dataset with every row in the right dataset)

### How Spark Performs Joins

Spark approaches cluster communication in two different ways during joins: shuffle join, which results in an 
all-to-all communication or a broadcast join.

#### Big table–to–big table

When you join a big table to another big table, you end up with a shuffle join.
In a shuffle join, every node talks to every other node and they share data according to which node has a certain 
key or set of keys (on which you are joining). These joins are expensive because the network can become congested 
with traffic, especially if your data is not partitioned well.

#### Big table–to–small table

When the table is small enough to fit into the memory of a single worker node, with some breathing room of course, 
we can optimize our join. Although we can use a big table–to–big table communication strategy, it can often be more 
efficient to use a broadcast join. What this means is that we will replicate our small DataFrame onto every worker 
node in the cluster (be it located on one machine or many). Now this sounds expensive. However, what this does is
prevent us from performing the all-to-all communication during the entire join process. Instead, we perform it only 
once at the beginning and then let each individual worker node perform the work without having to wait or 
communicate with any other worker node.

With the DataFrame API, we can also explicitly give the optimizer a hint that we would like to use a broadcast join 
by using the correct function around the small DataFrame in question:
```scala
val joinExpr = person.col("graduate_program") === graduateProgram.col("id")
person.join(broadcast(graduateProgram), joinExpr).explain()
```

The SQL interface also includes the ability to provide hints to perform joins. These are not
enforced, however, so the optimizer might choose to ignore them. You can set one of these hints
by using a special comment syntax. MAPJOIN, BROADCAST, and BROADCASTJOIN all do the same
thing and are all supported:
```sql
SELECT /*+ MAPJOIN(graduateProgram) */ * FROM person JOIN graduateProgram
ON person.graduate_program = graduateProgram.id
```
This doesn’t come for free either: if you try to broadcast something too large, you can crash your driver node 
(because that collect is expensive). This is likely an area for optimization in the future.

### Conclusion
In this chapter, we discussed joins, probably one of the most common use cases. One thing we did not mention but is 
important to consider is if you partition your data correctly prior to a join, you can end up with much more 
efficient execution because even if a shuffle is planned, if data from two different DataFrames is already located 
on the same machine, Spark can avoid the shuffle.