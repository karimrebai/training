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
SELECT ifnull(null, 'return_value'), -- return_value
       nullif('value', 'value'),     -- null
       nvl(null, 'return_value'),    -- return_value
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

val power3udf = udf(power3(_: Double): Double)

udfExampleDF.select(power3udf(col("num"))).show()

spark.udf.register("power3", power3(_: Double): Double)
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
df.groupBy("InvoiceNo").agg("Quantity" -> "avg", "Quantity" -> "stddev_pop").show()
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
val windowSpec = Window
        .partitionBy("CustomerId", "date")
        .orderBy(col("Quantity").desc).rowsBetween(Window.unboundedPreceding, Window.currentRow)
val maxPurchaseQuantity = max(col("Quantity")).over(windowSpec)
val purchaseDenseRank = dense_rank().over(windowSpec)
val purchaseRank = rank().over(windowSpec)

```

### Grouping Sets
Thus far in this chapter, we’ve seen simple group-by expressions that we can use to aggregate on
a set of columns with the values in those columns. However, sometimes we want something a bit
more complete—an aggregation across multiple groups. We achieve this by using grouping sets.
Grouping sets are a low-level tool for combining sets of aggregations together. They give you the
ability to create arbitrary aggregation in their group-by statements.

Let’s work through an example to gain a better understanding. Here, we would like to get the
total quantity of all stock codes and customers. To do so, we’ll use the following SQL
expression:
```
dfNoNull = dfWithDate.drop()
dfNoNull.createOrReplaceTempView("dfNoNull")

# You can do the exact same thing by using a grouping set:
SELECT CustomerId, stockCode, sum(Quantity) FROM dfNoNull
GROUP BY customerId, stockCode GROUPING SETS((customerId, stockCode))
ORDER BY CustomerId DESC, stockCode DESC
```
Simple enough, but what if you also want to include the total number of items, regardless of
customer or stock code? With a conventional group-by statement, this would be impossible. But,
it’s simple with grouping sets: we simply specify that we would like to aggregate at that level, as
well, in our grouping set. This is, effectively, the union of several different groupings together:
```
SELECT CustomerId, stockCode, sum(Quantity) FROM dfNoNull
GROUP BY customerId, stockCode GROUPING SETS((customerId, stockCode),())
ORDER BY CustomerId DESC, stockCode DESC
```

/!\ GROUPING SETS operator is only available in SQL. To perform the same in DataFrames, you use rollup
and cube operators.

#### Rollups
A rollup is a multidimensional aggregation that performs a variety of group-by style calculations for us.
Let’s create a rollup that looks across time (with our new Date column) and space (with the Country column)
and creates a new DataFrame that includes the grand total over all dates, the grand total for each date in
the DataFrame, and the subtotal for each country on each date in the DataFrame:
```
rolledUpDF = dfNoNull.rollup("Date", "Country").agg(sum("Quantity"))\
.selectExpr("Date", "Country", "`sum(Quantity)` as total_quantity")\
.orderBy("Date")
```

#### Cube
A cube takes the rollup to a level deeper. Rather than treating elements hierarchically, a cube
does the same thing across all dimensions.
```
from pyspark.sql.functions import sum
dfNoNull.cube("Date", "Country").agg(sum(col("Quantity")))\
.select("Date", "Country", "sum(Quantity)").orderBy("Date").show()
```

#### Grouping Metadata
Sometimes when using cubes and rollups, you want to be able to query the aggregation levels so
that you can easily filter them down accordingly. We can do this by using the grouping_id,
which gives us a column specifying the level of aggregation that we have in our result set.
```
dfNoNull.cube("customerId", "stockCode").agg(grouping_id(), sum("Quantity"))
.orderBy(expr("grouping_id()").desc)
```

#### Pivot
Pivots make it possible for you to convert a row into a column.
```
pivoted = dfWithDate.groupBy("date").pivot("Country").sum()
```

## Chapter 8. Joins

### Join types

- Inner joins (keep rows with keys that exist in the left and right datasets)
- Outer joins (keep rows with keys in either the left or right datasets)
- Left outer joins (keep rows with keys in the left dataset)
- Right outer joins (keep rows with keys in the right dataset)
- Left semi joins (keep the rows in the left, and only the left dataset where the key appears in the right dataset)
- Left anti joins (keep the rows in the left, and only the left dataset where they do not appear in the right
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
SELECT /*+ MAPJOIN(graduateProgram) */ *
FROM person
         JOIN graduateProgram
              ON person.graduate_program = graduateProgram.id
```

This doesn’t come for free either: if you try to broadcast something too large, you can crash your driver node
(because that collect is expensive). This is likely an area for optimization in the future.

### Conclusion

In this chapter, we discussed joins, probably one of the most common use cases. One thing we did not mention but is
important to consider is if you partition your data correctly prior to a join, you can end up with much more
efficient execution because even if a shuffle is planned, if data from two different DataFrames is already located
on the same machine, Spark can avoid the shuffle.

## Chapter 9. Data Sources

### The Structure of the Data Sources API

#### Basics of Reading Data

The foundation for reading data in Spark is the DataFrameReader. We access this through the SparkSession via the
read attribute: ```spark.read```
At a minimum, you must supply the DataFrameReader a path to from which to read:

```scala
spark.read.format("csv")
  .option("mode", "FAILFAST")
  .option("inferSchema", "true")
  .option("path", "path/to/file(s)")
  .schema(someSchema)
  .load()
```

Read modes specify what will happen when Spark does come across malformed records:

- **permissive**: Sets all fields to null when it encounters a corrupted record and places all corrupted records in a
  string column called _corrupt_record
- **dropMalformed**: Drops the row that contains malformed records
- **failFast**: Fails immediately upon encountering malformed records

#### Basics of Writing Data

After we have a DataFrameWriter, we specify three values: the format, a series of options, xand the save mode. At a
minimum, you must supply a path:

```scala
dataframe.write.format("csv")
  .option("mode", "OVERWRITE")
  .option("dateFormat", "yyyy-MM-dd")
  .option("path", "path/to/file(s)")
  .save()
```

Save modes:

- **append**: Appends the output files to the list of files that already exist at that location
- **overwrite**: Will completely overwrite any data that already exists there
- **errorIfExists**: Throws an error and fails the write if data or files already exist at the specified location
- **ignore**: If data or files exist at the location, do nothing with the current DataFrame

### JSON files

In Spark, when we refer to JSON files, we refer to line-delimited JSON files. This contrasts with files that have a
large JSON object or array per file.

The line-delimited versus multiline trade-off is controlled by a single option: multiLine.

Line-delimited JSON is actually a much more stable format because:

- It allows you to append to a file with a new record (rather than read in an entire file and then write it out)
- JSON objects have structure, and JavaScript (on which JSON is based) has at least basic types

### Parquet files

Parquet is an open source column-oriented data store that provides a variety of storage optimizations, especially
for analytics workloads. It provides columnar compression, which saves storage space and allows for reading
individual columns instead of entire files.

#### Reading Parquet Files

We can set the schema if we have strict requirements for what our DataFrame should look like. Oftentimes this is not
necessary because we can use schema on read, which is similar to the inferSchema with CSV files. However, with
Parquet files, this method is more powerful because the schema is built into the file itself (so no inference needed).

### ORC files

ORC is a self-describing, type-aware columnar file format designed for Hadoop workloads. It is optimized for large
streaming reads, but with integrated support for finding required rows quickly.

What is the difference between ORC and Parquet? For the most part, they’re quite similar; the fundamental difference
is that Parquet is further optimized for use with Spark, whereas ORC is further optimized for Hive.

### SQL Databases

SQL datasources are one of the more powerful connectors because there are a variety of systems to which you can
connect (as long as that system speaks SQL).

You’re going to need to begin considering things like authentication and connectivity (you’ll need to determine
whether the network of your Spark cluster is connected to the network of your database system).

#### Reading from SQL Databases

```scala
val pgDF = spark.read
  .format("jdbc")
  .option("driver", "org.postgresql.Driver")
  .option("url", "jdbc:postgresql://database_server")
  .option("dbtable", "schema.tablename")
  .option("user", "username")
  .option("password", "my-secret-password")
  .load()

pgDF.select("DEST_COUNTRY_NAME").distinct().show(5)
```

You’ll notice that there is already a schema, as well. That’s because Spark gathers this information from the table
itself and maps the types to Spark data types.

#### Query Pushdown

Spark makes a best-effort attempt to filter data in the database itself before creating the DataFrame. For example,
in the previous query, we can see from the query plan that it selects only the relevant column name from the table:

```scala
pgDF.select("DEST_COUNTRY_NAME").distinct().explain
```

== Physical Plan ==
*HashAggregate(keys=[DEST_COUNTRY_NAME#8108], functions=[])
+- Exchange hashpartitioning(DEST_COUNTRY_NAME#8108, 200)
+- *HashAggregate(keys=[DEST_COUNTRY_NAME#8108], functions=[])
+- *Scan JDBCRelation(flight_info) [numPartitions=1] ...

Spark can actually do better than this on certain queries. For example, if we specify a filter on our DataFrame,
Spark will push that filter down into the database. We can see this in the explain plan under PushedFilters.

```scala
pgDF.filter("DEST_COUNTRY_NAME in ('Anguilla', 'Sweden')").explain
```

== Physical Plan ==
*Scan JDBCRel... PushedFilters: [*In(DEST_COUNTRY_NAME, [Anguilla,Sweden])],
...

Spark can’t translate all of its own functions into the functions available in the SQL database in which you’re
working. Therefore, sometimes you’re going to want to pass an entire query into your SQL that will return the
results as a DataFrame:

```scala
val pushdownQuery = """(SELECT DISTINCT(DEST_COUNTRY_NAME) FROM flight_info) AS flight_info"""
val dbDataFrame = spark.read.format("jdbc")
  .option("url", url)
  .option("dbtable", pushdownQuery)
  .option("driver", driver)
  .load()
```

### Advanced I/O Concepts

#### Splittable File Types and Compression

Certain file formats are fundamentally “splittable.” This can improve speed because it makes it possible for Spark
to avoid reading an entire file, and access only the parts of the file necessary to satisfy your query.
In conjunction with this is a need to manage compression. Not all compression schemes are splittable.
How you store your data is of immense consequence when it comes to making your Spark jobs run smoothly.
We recommend Parquet with gzip compression.

#### Reading Data in Parallel

Multiple executors can read different files at the same time. In general, this means that when you read from a folder
with multiple files in it, each one of those files will become a partition in your DataFrame and be read in by
available executors in parallel.

#### Writing Data in Parallel

The number of files or data written is dependent on the number of partitions the DataFrame has at the time you write
out the data.

- Partitioning is a tool that allows you to control what data is stored (and where) as you write it. When you write a
  file
  to a partitioned directory (or table), you basically encode a column as a folder. What this allows you to do is skip
  lots
  of data when you go to read it in later, allowing you to read in only the data relevant to your problem instead of
  having to scan the complete dataset.

```scala
csvFile.limit(10).write.mode("overwrite").partitionBy("DEST_COUNTRY_NAME")
  .save("/tmp/partitioned-files.parquet")
```

- Bucketing is another file organization approach with which you can control the data that is specifically written
  to each file. This can help avoid shuffles later when you go to read the data because data with the same bucket ID
  will all be grouped together into one physical partition. This means that the data is prepartitioned according to
  how you expect to use that data later on, meaning you can avoid expensive shuffles when joining or aggregating.

```scala
val numberBuckets = 10
val columnToBucketBy = "count"
csvFile.write.format("parquet").mode("overwrite")
  .bucketBy(numberBuckets, columnToBucketBy).saveAsTable("bucketedFiles")
```

#### Managing File Size

Managing file sizes is an important factor not so much for writing data but reading it later on. When you’re writing
lots of small files, there’s a significant metadata overhead that you incur managing all of those files. Spark
especially does not do well with small files, although many file systems (like HDFS) don’t handle lots of small
files well, either. You might hear this referred to as the “small file problem.” The opposite is also true: you
don’t want files that are too large either, because it becomes inefficient to have to read entire blocks of data  
when you need only a few rows.
maxRecordsPerFile option allows to better control file sizes by controlling the number of records that are written
to each file:

```scala
df.write.option("maxRecordsPerFile", 5000)
```

## Chapter 10. Spark SQL

Spark SQL is arguably one of the most important and powerful features in Spark.
In a nutshell, with Spark SQL you can run SQL queries against views or tables organized into databases.

Spark SQL is intended to operate as an online analytic processing (OLAP) database, not an online transaction
processing (OLTP) database. This means that it is not intended to perform extremely low-latency queries.

### How to Run Spark SQL Queries

#### Spark SQL CLI

```shell
./bin/spark-sql
```

#### Spark’s Programmatic SQL Interface

Via the method sql on the SparkSession object:

- Lazily executed
- Powerful interface because some transformations that are much simpler to express in SQL code than in DataFrames

```scala
spark.sql(
  """SELECT user_id, department, first_name FROM professors
WHERE department IN
(SELECT name FROM department WHERE created_date >= '2016-01-01')""")
```

- Even more powerful, you can completely interoperate between SQL and DataFrames:

```scala
spark.read.json("/data/flight-data/json/2015-summary.json")
  .createOrReplaceTempView("some_sql_view") // DF => SQL
spark.sql(
    """SELECT DEST_COUNTRY_NAME, sum(count)
FROM some_sql_view GROUP BY DEST_COUNTRY_NAME
""")
  .where("DEST_COUNTRY_NAME like 'S%'").where("`sum(count)` > 10")
  .count() // SQL => DF
```

#### SparkSQL Thrift JDBC/ODBC Server

Spark provides a Java Database Connectivity (JDBC) interface by which either you or a remote program connects to the
Spark driver in order to execute Spark SQL queries. A common use case might be a for a business analyst to connect
business intelligence software like Tableau to Spark.

### Catalog

The highest level abstraction in Spark SQL is the Catalog. The Catalog is an abstraction for the storage of metadata
about the data stored in your tables as well as other helpful things like databases, tables, functions, and views.

### Tables

In Spark 2.X, tables always contain data. There is no notion of a temporary table, only a view, which does not
contain data. If you go to drop a table, you can risk losing the data when doing so.

#### Spark-Managed Tables

One important note is the concept of managed versus unmanaged tables. Tables store the data within the tables as well
as the data about the tables; that is, the metadata.
You can have Spark manage the metadata for a set of files as well as for the data.
When you define a table from files on disk, you are defining an unmanaged table.
When you use saveAsTable on a DataFrame, you are creating a managed table for which Spark will track of all of the
relevant information.

#### Creating Tables

```sparksql
CREATE TABLE flights
(
    DEST_COUNTRY_NAME   STRING,
    ORIGIN_COUNTRY_NAME STRING,
    count LONG
) USING JSON OPTIONS (path '/data/flight-data/json/2015-summary.json')

CREATE TABLE flights_from_select USING parquet AS
SELECT *
FROM flights

-- If we don't specify format via USING, we create a Hive compatible table:
CREATE TABLE IF NOT EXISTS flights_from_select AS
SELECT *
FROM flights

-- Writing a partitioned dataset:
CREATE TABLE partitioned_flights USING parquet PARTITIONED BY (DEST_COUNTRY_NAME) AS
SELECT DEST_COUNTRY_NAME,
       ORIGIN_COUNTRY_NAME
FROM flights
```

#### Creating External Tables

Spark will manage the table’s metadata; however, the files are not managed by Spark at all:

```sparksql
CREATE EXTERNAL TABLE hive_flights
(
    DEST_COUNTRY_NAME   STRING,
    ORIGIN_COUNTRY_NAME STRING
)
    ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LOCATION '/data/flight-data-hive/'
```

#### Refreshing Table Metadata

```sparksql
REFRESH table partitioned_flights

MSCK REPAIR TABLE partitioned_flights
```

#### Creating views

```sparksql
CREATE VIEW just_usa_view AS
SELECT *
FROM flights
WHERE dest_country_name = 'United States'

-- Like tables, you can create temporary views that are available only during the current session and are not
-- registered to a database:
CREATE
TEMP VIEW just_usa_view_temp AS
SELECT *
FROM flights
WHERE dest_country_name = 'United States'
```

## Chapter 11. Datasets

- DataFrames are Datasets of type Row.
- Datasets are a strictly Java Virtual Machine (JVM) language feature that work only with Scala and Java.
- An Encoder is used to map the domain-specific type T to Spark’s internal type system.
- When you use the Dataset API, for every row it touches, Spark converts the Spark Row format to the object you
  specified (a Case Class or Java class).
  This conversion slows down your operations but can provide more flexibility. You will notice a hit in performance
  but this is a far different order of magnitude from what you might see from something like a user-defined function
  (UDF) in Python, because the performance costs are not as extreme as switching programming languages.

### When to use Datasets

- When the operation(s) you would like to perform cannot be expressed using DataFrame manipulations:
  You might have a large set of business logic that you’d like to encode in one specific function instead of in
  SQL or DataFrames.

- When you want or need type-safety, and you’re willing to accept the cost of performance to achieve it:
  Operations that are not valid for their types, say subtracting two string types, will fail at compilation time not
  at runtime. If correctness and bulletproof code is your highest priority, at the cost of some performance, this
  can be a great choice for you. This does not protect you from malformed data but can allow more elegance.

### Creating Datasets

#### In Java: Encoders

```java
public class Flight implements Serializable {
    String DEST_COUNTRY_NAME;
    String ORIGIN_COUNTRY_NAME;
    Long DEST_COUNTRY_NAME;
}

    Dataset<Flight> flights = spark.read.parquet("/data/flight-data/parquet/2010-summary.parquet/")
            .as(Encoders.bean(Flight.class));
```

#### In Scala: Case Classes

Case classes advantages:

- **Immutability** frees you from needing to keep track of where and when things are mutated.
- **Comparison-by-value** allows you to compare instances as if they were primitive values —no more uncertainty
  regarding whether instances of a class are compared by value or reference.
- **Pattern matching** simplifies branching logic, which leads to less bugs and more readable code.

```scala
case class Flight(DEST_COUNTRY_NAME: String, ORIGIN_COUNTRY_NAME: String, count: BigInt)

val flightsDF = spark.read.parquet("/data/flight-data/parquet/2010-summary.parquet/")
val flights = flightsDF.as[Flight]
```

### Actions

#### Filtering

You’ll notice in the following example that we’re going to create a function to define this filter.
By specifying a function, we are forcing Spark to evaluate this function on every row in our Dataset. This can be
very resource intensive. For simple filters it is always preferred to write SQL expressions.

```scala
def originIsDestination(flight_row: Flight): Boolean = {
  return flight_row.ORIGIN_COUNTRY_NAME == flight_row.DEST_COUNTRY_NAME
}

flights.filter(flight_row => originIsDestination(flight_row)).first()
```

#### Mapping

```scala
val destinations = flights.map(f => f.DEST_COUNTRY_NAME)
val localDestinations = destinations.take(5)
```

This might feel trivial and unnecessary; we can do the majority of this right on DataFrames. We in fact recommend
that you do this because you gain so many benefits from doing so. You will gain advantages like code generation that
are simply not possible with arbitrary user-defined functions.

### Joins

```scala
case class FlightMetadata(count: BigInt, randomData: BigInt)

val flightsMeta = spark.range(500).map(x => (x, scala.util.Random.nextLong))
  .withColumnRenamed("_1", "count").withColumnRenamed("_2", "randomData")
  .as[FlightMetadata]
val flights2 = flights
  .joinWith(flightsMeta, flights.col("count") === flightsMeta.col("count"))

flights2.selectExpr("_1.DEST_COUNTRY_NAME")

// Regular join work quite well too, ends up with a Dataframe
val flights2 = flights.join(flightsMeta, Seq("count"))
// No problem to join Datasets and Dataframes:
val flights2 = flights.join(flightsMeta.toDF(), Seq("count"))
```

### Grouping and Aggregations

```scala
//With groupBy, you loose types:
flights.groupBy("DEST_COUNTRY_NAME").count()
// Instead:
flights.groupByKey(x => x.DEST_COUNTRY_NAME).count()
// Although this provides flexibility, it’s a trade-off because now we are introducing JVM types as
// well as functions that cannot be optimized by Spark

def grpSum(countryName: String, values: Iterator[Flight]) = {
  values.dropWhile(_.count < 5).map(x => (countryName, x))
}
flights.groupByKey(x => x.DEST_COUNTRY_NAME).flatMapGroups(grpSum).show(5)
```

# Part IV. Production Applications

## Chapter 15. How Spark runs on a cluster

### The Architecture of a Spark Application

- The Spark driver

It is the controller of the execution of a Spark Application and maintains all of the state of the Spark cluster.
It must interface with the cluster manager in order to actually get physical resources and launch executors. At the
end of the day, this is just a process on a physical machine that is responsible for maintaining the state of the
application running on the cluster.

- The Spark executors
  Spark executors are the processes that perform the tasks assigned by the Spark driver. Executors have one core
  responsibility: take the tasks assigned by the driver, run them, and report back their state (success or failure)
  and results. Each Spark Application has its own separate executor processes.

- The cluster manager
  The cluster manager is responsible for maintaining a cluster of machines that will run your Spark Application(s).
  Somewhat confusingly, a cluster manager will have its own “driver ” (sometimes called master) and “worker”
  abstractions. The core difference is that these are tied to physical machines rather than processes.

![cluster_architecture.png](images%2Fcluster_architecture.png)

#### Execution modes

- Cluster mode: The cluster manager then launches the driver process on a worker node inside the cluster, in
  addition to the executor processes. This means that the cluster manager is responsible for maintaining all Spark
  Application–related processes. The cluster manager places the driver on a worker node and the executors on other
  worker nodes.
- Client mode: Client mode is nearly the same as cluster mode except that the Spark driver remains on the client
  machine that submitted the application. This means that the client machine is responsible for maintaining the
  Spark driver process, and the cluster manager maintains the executor processses. Driver is in a machine that is not
  colocated on the cluster. These machines are commonly referred to as gateway machines or edge nodes.
- Local mode: Local mode is a significant departure from the previous two modes: it runs the entire Spark
  Application on a single machine. It achieves parallelism through threads on that single machine.

### The Life Cycle of a Spark Application (Outside Spark)

#### Client Request

The first step is for you to submit an actual application. This will be a pre-compiled JAR or library. At this point,
you are executing code on your local machine and you’re going to make a request to the cluster manager driver node.
Here, we are explicitly asking for resources for the Spark driver process only.

```shell
./bin/spark-submit \
--class <main-class> \
--master <master-url> \
--deploy-mode cluster \
--conf <key>=<value> \
... # other options
<application-jar> \
[application-arguments]
```

#### Launch

Now that the driver process has been placed on the cluster, it begins running user code. This code must include a
SparkSession that initializes a Spark cluster (e.g., driver + executors). The SparkSession will subsequently
communicate with the cluster manager, asking it to launch Spark executor processes across the cluster.
The cluster manager responds by launching the executor processes (assuming all goes well) and sends the relevant
information about their locations to the driver process.

#### Execution

The driver and the workers communicate among themselves, executing code and moving data around. The driver schedules
tasks onto each worker, and each worker responds with the status of those tasks and success or failure.

#### Completion

After a Spark Application completes, the driver processs exits with either success or failure. The cluster manager then
shuts down the executors in that Spark cluster for the driver. At this point, you can see the success or failure of
the Spark Application by asking the cluster manager for this information.

### The Life Cycle of a Spark Application (Inside Spark)

Each application is made up of one or more Spark jobs. Spark jobs within an application are executed serially
(unless you use threading to launch multiple actions in parallel).

#### The SparkSession

The first step of any Spark Application is creating a SparkSession:

```scala
val spark = SparkSession.builder()
  .appName("Databricks Spark Example")
  .config("spark.sql.warehouse.dir", "/user/hive/warehouse")
  .getOrCreate()
```

A SparkContext object within the SparkSession represents the connection to the Spark cluster. This class is how you
communicate with some of Spark’s lower-level APIs, such as RDDs. Through a SparkContext, you can create RDDs,
accumulators, and broadcast variables, and you can run code on the cluster.

#### Logical Instructions

Spark code essentially consists of transformations and actions.
Understanding how we take declarative instructions like DataFrames and convert them into physical execution plans is
an important step to understanding how Spark runs on a cluster.

```python
df1 = spark.range(2, 10000000, 2)
df2 = spark.range(2, 10000000, 4)
step1 = df1.repartition(5)
step12 = df2.repartition(6)
step2 = step1.selectExpr("id * 5 as id")
step3 = step2.join(step12, ["id"])
step4 = step3.selectExpr("sum(id)")
step4.collect()  # 2500000000000
step4.explain()
```

When you run this code, we can see that your action triggers one complete Spark job. Let’s take a look at the
explain plan to ground our understanding of the physical execution plan. We can access this information on the SQL
tab in the Spark UI, as well:
![physical_plan.png](images%2Fphysical_plan.png)

#### A Spark Job

In general, there should be one Spark job for one action. Actions always return results.
Each job breaks down into a series of stages, **the number of which depends on how many shuffle operations need to
take place**.
This job breaks down into the following stages and tasks:

- Stage 1 with 8 Tasks
- Stage 2 with 8 Tasks
- Stage 3 with 6 Tasks
- Stage 4 with 5 Tasks
- Stage 5 with 200 Tasks
- Stage 6 with 1 Task

#### Stages

Stages in Spark represent groups of tasks that can be executed together to compute the same operation on multiple
machines. In general, Spark will try to pack as much work as possible (i.e., as many transformations as possible
inside your job) into the same stage, but the engine starts new stages after operations called shuffles. A shuffle
represents a physical repartitioning of the data—for example, sorting a DataFrame, or grouping data that was loaded
from a file by key (which requires sending records with the same key to the same node).

In the job we looked at earlier, the first two stages correspond to the range that you perform in order to create 
your DataFrames. By default when you create a DataFrame with range, it has eight partitions. The next step is the 
repartitioning. This changes the number of partitions by shuffling the data. These DataFrames are shuffled into six 
partitions and five partitions, corresponding to the number of tasks in stages 3 and 4.

Stages 3 and 4 perform on each of those DataFrames and the end of the stage represents the join (a shuffle). 
Suddenly, we have 200 tasks. This is because of a Spark SQL configuration. The spark.sql.shuffle.partitions default 
value is 200, which means that when there is a shuffle performed during execution, it outputs 200 shuffle partitions 
by default. You can change this value, and the number of output partitions will change.

A good rule of thumb is that the number of partitions should be larger than the number of executors on your cluster, 
potentially by multiple factors depending on the workload. If you are running code on your local machine, it would 
behoove you to set this value lower because your local machine is unlikely to be able to execute that number of 
tasks in parallel. This is more of a default for a cluster in which there might be many more executor cores to use. 
Regardless of the number of partitions, that entire stage is computed in parallel. The final result aggregates those
partitions individually, brings them all to a single partition before finally sending the final result to the driver.

#### Tasks
Stages in Spark consist of tasks. Each task corresponds to a combination of blocks of data and a set of 
transformations that will run on a single executor. If there is one big partition in our dataset, we will have one 
task. If there are 1,000 little partitions, we will have 1,000 tasks that can be executed in parallel. A task is 
just a unit of computation applied to a unit of data (the partition). Partitioning your data into a greater number 
of partitions means that more can be executed in parallel. This is not a panacea, but it is a simple place to begin 
with optimization.

### Execution Details

#### Pipelining
Spark performs as many steps as it can at one point in time before writing data to memory or disk.
With pipelining, any sequence of operations that feed data directly into each other, without needing to move it 
across nodes, is collapsed into a single stage of tasks that do all the operations together:
Ex: map -> filter -> map
This pipelined version of the computation is much faster than writing the intermediate results to memory or disk 
after each step.

#### Shuffle Persistence
When Spark needs to run an operation that has to move data across nodes, such as a reduce-by-key operation (where input
data for each key needs to first be brought together from many nodes), the engine can’t perform pipelining anymore, 
and instead it performs a cross-network shuffle. Spark always executes shuffles by first having the “source” tasks 
(those sending data) write shuffle files to their local disks during their execution stage. Then, the stage that 
does the grouping and reduction launches and runs tasks that fetch their corresponding records from each shuffle 
file and performs that computation (e.g., fetches and processes the data for a specific range of keys). Saving the 
shuffle files to disk lets Spark run this stage later in time than the source stage (e.g., if there are not enough 
executors to run both at the same time), and also lets the engine re-launch reduce tasks on failure without 
rerunning all the input tasks.


## Chapter 17. Deploying Spark

### Where to Deploy Your Cluster to Run Spark Applications

#### On-Premises Cluster Deployments

If you make your cluster too small, it will be hard to launch the occasional very large analytics query or training 
job for a new machine learning model, whereas if you make it large, you will have resources sitting idle.

Second, for on-premises clusters, you need to select and operate your own storage system, such as a Hadoop file system
or scalable key-value store. This includes setting up georeplication and disaster recovery if required.

#### Spark in the Cloud

- Resources can be launched and shut down elastically, so you can run that occasional “monster” job that takes
  hundreds of machines for a few hours without having to pay for them all the time. Even for normal operation, you 
  can choose a different type of machine and cluster size for each application to optimize its cost performance— for 
  example, launch machines with Graphics Processing Units (GPUs) just for your deep learning jobs.

- Public clouds include low-cost, georeplicated storage that makes it easier to manage large amounts of data.

- Many companies looking to migrate to the cloud imagine they’ll run their applications in the same way that they 
  run their on-premises clusters.
  All major cloud providers include managed Hadoop clusters, which provides HDFS for storage as well as Spark.
  This is actually not a great way to run Spark in the cloud, however, because by using a fixed-size cluster and 
  file system, you are not going to be able to take advantage of elasticity. Instead, it is generally a better idea 
  to use global storage systems that are decoupled from a specific cluster, such as Amazon S3, Azure Blob Storage, 
  or Google Cloud Storage and spin up machines dynamically for each Spark workload.
  Basically, keep in mind that running Spark in the cloud need not mean migrating an on-premises installation to 
  virtual machines: you can run Spark natively against cloud storage to take full advantage of the cloud’s 
  elasticity, cost-saving benefit, and management tools without having to manage an on-premise computing stack 
  within your cloud environment.

#### Application Scheduling

- Cluster managers provide the facilities for scheduling across Spark applications. Within each Spark application, 
multiple jobs (i.e., Spark actions) may be running concurrently if they were submitted by different threads.

- If multiple users need to share your cluster and run different Spark Applications, there are
  different options to manage allocation, depending on the cluster manager:
  - The simplest option, available on all cluster managers, is static partitioning of resources. With this approach, 
    each application is given a maximum amount of resources that it can use, and holds onto those resources for the 
    entire duration.
  - In addition, dynamic allocation can be turned on to let applications scale up and down dynamically based on 
    their current number of pending tasks. This means that your application can give resources back to the cluster if
    they are no longer used, and request them again later when there is demand.
    This feature is disabled by default
    There are two requirements for using this feature. First, your application must set spark.dynamicAllocation.
    enabled to true. Second, you must set up an external shuffle service on each worker node in the same cluster and 
    set spark.shuffle.service.enabled to true in your application. The purpose of the external shuffle service is to 
    allow executors to be removed without deleting shuffle files written by them.

### Miscellaneous considerations

- Depending on your workload, it might be worth considering using Spark’s external shuffle service. Typically Spark 
stores shuffle blocks (shuffle output) on a local disk on that particular node. An external shuffle service allows 
for storing those shuffle blocks so that they are available to all executors, meaning that you can arbitrarily kill 
executors and still have their shuffle outputs available to other applications.


## Chapter 18. Monitoring and Debugging

### Debugging and Spark First Aid

#### Slow Tasks or Stragglers

This issue is quite common when optimizing applications, and can occur either due to work not being evenly 
distributed across your machines (“skew”), or due to one of your machines being slower than the others (e.g., due to 
a hardware problem).

One particularly common case is that you use a group-by-key operation and one of the keys just has more data than 
others. In this case, when you look at the Spark UI, you might see that the shuffle data for some nodes is much 
larger than for others.

- Try increasing the number of partitions to have less data per partition.
- Try repartitioning by another combination of columns. For example, stragglers can come up when you partition by a 
  skewed ID column, or a column where many values are null. In the latter case, it might make sense to first filter 
  out the null values.
- Try increasing the memory allocated to your executors if possible.

#### Slow Aggregations

Unfortunately, this issue can’t always be solved. Sometimes, the data in your job just has some skewed keys, and the 
operation you want to run on them needs to be slow.

- Increasing the number of partitions, prior to an aggregation, might help by reducing the number of different keys 
  processed in each task.
- Increasing executor memory can help. If a single key has lots of data, this will allow its executor to spill to 
  disk less often and finish faster, although it may still be much slower than executors processing other keys.
- Ensuring that all filters and SELECT statements that can be are above the aggregation can help to ensure that 
  you’re working only on the data that you need to be working on and nothing else. Spark’s query optimizer will 
  automatically do this for the structured APIs.
- Ensure null values are represented correctly (using Spark’s concept of null) and not as default value like " " or 
  "EMPTY". Spark often optimizes for skipping nulls early in the job when possible, but it can’t do so for your 
  own placeholder values.

#### Slow Joins

Joins and aggregations are both shuffles, so they share some of the same general symptoms as well as treatments.

- Many joins can be optimized (manually or automatically) to other types of joins.
- Experimenting with different join orderings can really help speed up jobs, especially if some of those joins 
  filter out a large amount of data.
- Partitioning a dataset prior to joining can be very helpful for reducing data movement across the cluster, 
  especially if the same dataset will be used in multiple join operations.
- Slow joins can also be caused by data skew. There’s not always a lot you can do here, but sizing up the Spark 
  application and/or increasing the size of executors can help, as described in earlier sections.
- Ensuring that all filters and select statements that can be are above the join can help to ensure that you’re 
  working only on the data that you need for the join.

#### Slow Reads and Writes

Slow I/O can be difficult to diagnose, especially with networked file systems.

- Turning on speculation (set spark.speculation to true) can help with slow reads and writes. This will launch 
  additional tasks with the same operation in an attempt to see whether it’s just some transient issue in the first 
  task. Speculation is a powerful tool and works well with consistent file systems. However, it can cause duplicate 
  data writes with some eventually consistent cloud services, such as Amazon S3, so check whether it is supported by 
  the storage system connector you are using.
- Ensuring sufficient network connectivity can be important—your Spark cluster may simply not have enough total 
  network bandwidth to get to your storage system.

#### Driver OutOfMemoryError or Driver Unresponsive

This is usually a pretty serious issue because it will crash your Spark Application. It often happens due to 
collecting too much data back to the driver, making it run out of memory.

- Your code might have tried to collect an overly large dataset to the driver node using operations such as collect.
- You might be using a broadcast join where the data to be broadcast is too big. Use Spark’s maximum broadcast join 
  configuration to better control the size it will broadcast.
- A long-running application generated a large number of objects on the driver and is unable to release them. Java’s 
  jmap tool can be useful to see what objects are filling most of the memory of your driver JVM by printing a 
  histogram of the heap.
- Increase the driver’s memory allocation if possible to let it work with more data.

#### Executor OutOfMemoryError or Executor Unresponsive

- Try increasing the memory available to executors and the number of executors.
- Try increasing PySpark worker size via the relevant Python configurations.
- Look for garbage collection error messages in the executor logs. Some of the tasks that are running, especially if 
  you’re using UDFs, can be creating lots of objects that need to be garbage collected. Repartition your data to 
  increase parallelism, reduce the amount of records per task, and ensure that all executors are getting the same 
  amount of work.
- Ensure that null values are handled correctly (that you’re using null) and not some default value like " " or "EMPTY".
- This is more likely to happen with RDDs or with Datasets because of object instantiations. Try using fewer UDFs 
  and more of Spark’s structured operations when possible.


## Chapter 19. Performance Tuning

### Indirect Performance Enhancements

#### Design Choices

- Scala versus Java versus Python versus R
  - If you want to perform some single-node machine learning after performing a large ETL job, we might recommend 
    running your ETL code as SparkR code and then using R’s massive machine learning ecosystem to run your 
    single-node machine learning algorithms.
  - Things do get a bit more complicated when you need to include custom transformations that cannot be created in 
    the Structured APIs. These might manifest themselves as RDD transformations or UDFs. If  you’re going to do this,
    R and Python are not necessarily the best choice simply because of how this is actually executed.
  - We find that using Python for the majority of the application, and porting some of it to Scala or writing 
    specific UDFs in Scala as your application evolves, is a powerful technique—it allows for a nice balance between 
    overall usability, maintainability, and performance.
- DataFrames versus SQL versus Datasets versus RDDs
  - Across all languages, DataFrames, Datasets, and SQL are equivalent in speed. This means that if you’re using
    DataFrames in any of these languages, performance is equal. However, if you’re going to be defining UDFs, you’ll 
    take a performance hit writing those in Python or R, and to some extent a lesser  performance hit in Java and Scala.
  - Although all DataFrame, SQL, and Dataset code compiles down to RDDs, Spark’s optimization engine will write 
    “better” RDD code than you can manually and certainly do it with orders of magnitude less effort.

#### Object Serialization in RDDs

When you’re working with custom data types, you’re going to want to serialize them using Kryo because it’s both more 
compact and much more efficient than Java serialization. However, this does come at the inconvenience of registering 
the classes that you will be using in your application.

#### Cluster Configurations

- Dynamic allocation
  Spark provides a mechanism to dynamically adjust the resources your application occupies based on the workload. 
  This means that your application can give resources back to the cluster if they are no longer used, and request 
  them again later when there is demand. This feature is particularly useful if multiple applications share 
  resources in your Spark cluster.

#### Scheduling

Scheduling optimizations do involve some research and experimentation, and unfortunately there are not super-quick 
fixes beyond setting spark.scheduler.mode to FAIR to allow better sharing of resources across multiple users, or 
setting --max-executor-cores, which specifies the maximum number of executor cores that your application will need. 
Specifying this value can ensure that your application does not take up all the resources on the cluster.

#### Data at Rest

Making sure that you’re storing your data for effective reads later on is absolutely essential to successful big data
projects. This involves choosing your storage system, choosing your data format, and taking advantage of features 
such as data partitioning in some storage formats.

- File-based long-term data storage
  - One of the easiest ways to optimize your Spark jobs is to follow best practices when storing data and choose the 
    most efficient storage format possible.
  - Generally you should always favor structured, binary types to store your data, especially when you’ll be 
    accessing it frequently.
  - The most efficient file format you can generally choose is Apache Parquet. Parquet stores data in binary files 
    with column-oriented storage, and also tracks some statistics about each file that make it possible to quickly 
    skip data not needed for a query.

- Splittable file types and compression
  - Whatever file format you choose, you should make sure it is “splittable”, which means that different tasks can 
    read different parts of the file in parallel. When read in the file, all cores are able to do part of the work.
    If the file is not splittable—say something like a malformed JSON file—we’re going to need to read in the entire 
    file on a single machine, greatly reducing parallelism.
  - The main place splittability comes in is compression formats. A ZIP file or TAR archive cannot be split, which 
    means that even if we have 10 files in a ZIP file and 10 cores, only one core can
    read in that data because we cannot parallelize access to the ZIP file. This is a poor use of resources. In 
    contrast, files compressed using gzip, bzip2, or lz4 are generally splittable if they were written by a parallel 
    processing framework like Hadoop or Spark. For your own input data, the simplest way to make it splittable is to 
    upload it as separate files, ideally each no larger than a few hundred megabytes.

- Table partitioning
  It refers to storing files in separate directories based on a key, such as the date field in the data. Storage 
  managers like Apache Hive support this concept, as do many of Spark’s built-in data sources. Partitioning your 
  data correctly allows Spark to skip many irrelevant files when it only requires data with a specific range of keys.
  For instance, if users frequently filter by “date” or “customerId” in their queries, partition your data by those 
  columns. This will greatly reduce the amount of data that end users must read by most queries, and therefore 
  dramatically increase speed.
  The one downside of partitioning, however, is that if you partition at too fine a granularity, it can result in 
  many small files, and a great deal of overhead trying to list all the files in the storage system.

- Bucketing
  Bucketing your data allows Spark to “pre-partition” data according to how joins or aggregations are likely to 
  be performed by readers. This can improve performance and stability because data can be consistently distributed 
  across partitions as opposed to skewed into just one or two. For instance, if joins are frequently performed on a 
  column immediately after a read, you can use bucketing to ensure that the data is well partitioned according to 
  those values. This can help prevent a shuffle before a join and therefore help speed up data access.

- The number of files
  If there are lots of small files, you’re going to pay a price listing and fetching each of those individual files.
  Having lots of small files is going to make the scheduler work much harder to locate the data and launch all of 
  the read tasks. This can increase the network and scheduling overhead of the job. Having fewer large files eases 
  the pain off the scheduler but it will also make tasks run longer. In this case, though, you can always launch 
  more tasks than there are input files if you want more parallelism—Spark will split each file across multiple tasks
  assuming you are using a splittable format.
  In general, we recommend sizing your files so that they each contain at least a few tens of megatbytes of data.
  One way of controlling data partitioning when you write your data is through the option maxRecordsPerFile.

#### Memory Pressure and Garbage Collection

During the course of running Spark jobs, the executor or driver machines may struggle to complete their tasks
because of a lack of sufficient memory or “memory pressure.” This may occur when an application takes up too much 
memory during execution or when garbage collection runs too frequently or is slow to run as large numbers of objects 
are created in the JVM and subsequently garbage collected as they are no longer used. One strategy for easing this
issue is to ensure that you’re using the Structured APIs as much as possible. These will not only increase the 
efficiency with which your Spark jobs will execute, but it will also greatly reduce memory pressure because JVM 
objects are never realized and Spark SQL simply performs the computation on its internal format.

- Measuring the impact of garbage collection
  The first step in garbage collection tuning is to gather statistics on how frequently garbage collection occurs 
  and the amount of time it takes. You can do this by adding -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
  to Spark’s JVM options using the spark.executor.extraJavaOptions configuration parameter.

- Garbage collection tuning
  - Java heap space is divided into two regions: Young and Old. The Young generation is meant to hold short-lived 
  objects whereas the Old generation is intended for objects with longer lifetimes. The Young generation is further
  divided into three regions: Eden, Survivor1, and Survivor2.
  - The goal of garbage collection tuning in Spark is to ensure that only long-lived cached datasets are stored in the 
  Old generation and that the Young generation is sufficiently sized to store all short-lived objects.
  - If a full garbage collection is invoked multiple times before a task completes, it means that there isn’t enough 
  memory available for executing tasks, so you should decrease the amount of memory Spark uses for caching
  (spark.memory.fraction).
  - If there are too many minor collections but not many major garbage collections, allocating more memory for Eden 
    would help. You can set the size of the Eden to be an over-estimate of how much memory each task will need. If 
    the size of Eden is determined to be E, you can set the size of the Young generation using the option -Xmn=4/3*E.


### Direct Performance Enhancements

#### Parallelism
The first thing you should do whenever trying to speed up a specific stage is to increase the degree of parallelism. 
In general, we recommend having at least two or three tasks per CPU core in your cluster if the stage processes a 
large amount of data. You can set this via the spark.default.parallelism property as well as tuning the 
spark.sql.shuffle.partitions according to the number of cores in your cluster.

#### Improved Filtering
Another frequent source of performance enhancements is moving filters to the earliest part of your Spark job that 
you can. Sometimes, these filters can be pushed into the data sources themselves and this means that you can avoid 
reading and working with data that is irrelevant to your end result. Enabling partitioning and bucketing also helps 
achieve this.

#### Repartitioning and Coalescing
Repartition calls can incur a shuffle. However, doing some can optimize the overall execution of a job by balancing 
data across the cluster, so they can be worth it. In general, you should try to shuffle the least amount of data 
possible. For this reason, if you’re reducing the number of overall partitions in a DataFrame or RDD, first try 
coalesce method, which will not perform a shuffle but rather merge partitions on the same node into one partition. 
The slower repartition method will also shuffle data across the network to achieve even load balancing. Repartitions can
be particularly helpful when performing joins or prior to a cache call. Remember that repartitioning is not free, 
but it can improve overall application performance and parallelism of your jobs.

#### User-Defined Functions (UDFs)
In general, avoiding UDFs is a good optimization opportunity. UDFs are expensive because they force representing 
data as objects in the JVM and sometimes do this multiple times per record in a query. You should try to use the 
Structured APIs as much as possible to perform your manipulations simply because they are going to perform the 
transformations in a much more efficient manner than you can do in a high-level language.

#### Temporary Data Storage (Caching)
In applications that reuse the same datasets over and over, one of the most useful optimizations is caching. Caching 
will place a DataFrame, table, or RDD into temporary storage (either memory or disk) across the executors in your 
cluster, and make subsequent reads faster. Although caching might sound like something we should do all the time, 
it’s not always a good thing to do. That’s because caching data incurs a serialization, deserialization, and storage 
cost.

Caching is a lazy operation, meaning that things will be cached only as they are accessed.

The cache command in Spark always places data in memory by default, caching only part of the dataset if the 
cluster’s total memory is full. For more control, there is also a persist method that takes a StorageLevel object to 
specify where to cache the data: in memory, on disk, or both.

- MEMORY_ONLY (default): Store RDD as deserialized Java objects in the JVM. If the RDD does not fit in memory, some 
  partitions will not be cached and will be recomputed on the fly each time they’re needed.

- MEMORY_AND_DISK: Store RDD as deserialized Java objects in the JVM. If the RDD does not fit in memory, store the 
  partitions that don’t fit on disk, and read them from there when they’re needed.

- MEMORY_ONLY_SER (Java and Scala): Store RDD as serialized Java objects. This is generally more space-efficient 
  than deserialized objects, especially when using a fast serializer, but more CPU-intensive to read.

- MEMORY_AND_DISK_SER (Java and Scala): Similar to MEMORY_ONLY_SER , but spill partitions that don’t fit in memory 
  to disk instead of recomputing them on the fly each time they’re needed.

- DISK_ONLY: Store the RDD partitions only on disk.

- MEMORY_ONLY_2, MEMORY_AND_DISK_2 etc: Same as the previous levels, but replicate each partition on 2 cluster nodes.

- OFF_HEAP: Similar to MEMORY_ONLY_SER, but store the data in off-heap memory.

#### Joins
- Equi-joins are the easiest for Spark to optimize at this point and therefore should be preferred wherever possible.

- Trying to use the filtering ability of inner joins by changing join ordering can yield large speedups.

- Using broadcast join hints can help Spark make intelligent planning decisions when it comes to creating query plans.

- Collecting statistics on tables prior to a join will help Spark make intelligent join decisions.

#### Aggregations
For the most part, there are not too many ways that you can optimize specific aggregations beyond filtering data 
before the aggregation having a sufficiently high number of partitions.
However, if you’re using RDDs, controlling exactly how these aggregations are performed (e.g., using reduceByKey 
when possible over groupByKey) can be very helpful and improve the speed and stability of your code.

#### Broadcast Variables
The basic premise is that if some large piece of data will be used across multiple UDF calls in your program, you 
can broadcast it to save just a single read-only copy on each node and avoid re-sending this data with each job.
For example, broadcast variables may be useful to save a lookup table or a machine learning model.
You can also broadcast arbitrary objects by creating broadcast variables using your SparkContext, and then simply 
refer to those variables in your tasks.


### Conclusion
There are many different ways to optimize the performance of your Spark Applications and make them run faster and at 
a lower cost. In general, the main things you’ll want to prioritize are:
1) Reading as little data as possible through partitioning and efficient binary formats.
2) Making sure there is sufficient parallelism and no data skew on the cluster using partitioning.
3) Using high-level APIs such as the Structured APIs as much as possible to take already optimized code
