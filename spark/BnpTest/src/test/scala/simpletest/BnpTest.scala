package simpletest

import com.google.common.io.Resources.getResource
import com.holdenkarau.spark.testing.DatasetSuiteBase
import org.apache.spark.sql.functions.avg
import org.scalatest.{FlatSpec, Matchers}
import org.apache.spark.sql.functions._

case class CustomerData(
                         customerId: String,
                         forename: String,
                         surname: String
                       )

case class AccountData(
                        customerId: String,
                        accountId: String,
                        balance: Long
                      )

case class CustomerAccountOutput(
                                  customerId: String,
                                  forename: String,
                                  surname: String,
                                  //Accounts for this customer
                                  accounts: Seq[AccountData],
                                  //Statistics of the accounts
                                  numberAccounts: Int,
                                  totalBalance: Long,
                                  averageBalance: Double
                                )

class BnpTest extends FlatSpec with Matchers with DatasetSuiteBase {
  "Simple test" should "do something" in {
    val sqlCtx = sqlContext
    import sqlCtx.implicits._

    //Get file paths from files stored in project resources
    val customerCSV = getResource("customer_data.csv").getPath
    val accountCSV = getResource("account_data.csv").getPath

    //Create DataFrames of sources
    val customerDF = spark.read.option("header", "true")
      .csv(customerCSV)
    val accountDF = spark.read.option("header", "true")
      .csv(accountCSV)

    //Create Datasets of sources
    val customerDS = customerDF.as[CustomerData]
    val accountDS = accountDF.withColumn("balance", 'balance.cast("long")).as[AccountData]

    val result = customerDS.joinWith(accountDS, customerDS("customerId") === accountDS("customerId"))
      .groupBy(customerDS("customerId"))
      .agg(count(customerDS("customerId")), sum(accountDS("balance")), avg(accountDS("balance")))
      .show()

    // result.count() shouldBe 3
  }
}
