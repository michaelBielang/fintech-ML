## Introduction

This project ist part of my bachelor thesis with the title "Execution and analysis of finance transactions done by algorithms"


## App 

### Features

 * [x] Fetching chart data from crypto exchange (www.poloniex.com)
 * [x] Make use of trading indicators  (Demo-mode uses MACD and RSI)
 * [x] Prepare and store data for in-app communication with Matlab
 * [x] Fully functional Matlab implementation for linear and stepwise regression analyse, prediction and backtesting
 * [x] Building and maintaining data structures via liquibase
 * [x] Embedded & fast H2 DB
 * [x] Test Coverage
 * [x] Support of more than 130 technical indicators (Aroon, ATR, moving averages, parabolic SAR, RSI, etc.)
 * [x] Utilities to run and compare strategies
 * [x] API support of Matlab, Apache-Spark and DeepLearning4j
 
### Todos

 - Further implementation of Apache Spark Neural Networks
 - Implement ML with Matlab
 - Implement an UI (React support already present)
 
### Setup

To use this demo it is required to have the following software installed

  - Java 14+
  - Apache-Hadoop
  - Matlab R2020a
  
Moreover, Matlab requires to have these plugins installed:

  - Financial Instruments Toolbox
  - Trading Toolbox
  - Symbolic Math Toolbox
  - Statistics and Machine Learning Toolbox
  - Simulink  
  - Optimization Toolbox
  - Financial Toolbox

This app was tested on a Windows 10 machine.

To set up Hadoop on Windows 10 please follow [these](https://towardsdatascience.com/installing-hadoop-3-2-1-single-node-cluster-on-windows-10-ac258dd48aef) instructions.

Clone this program and import it with an IDE your choice. (I use IntelliJ IDEA 2020.2)

If your maven configuration does not import the dependencies please do so manually. 

To verify a proper setup please execute the verify lifecycle in maven. 
It should state `BUILD SUCCESS`

To finally execute this application execute the main function within the `AlgorithmicTradingApplication.class`

## Technology Stack

### UI

+ React (dependencies, internal app routing and basic layout)
+ MATLAB plot

### Maven dependencies

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.liquibase</groupId>
            <artifactId>liquibase-core</artifactId>
            <version>3.10.1</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <scope>runtime</scope>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-core</artifactId>
            <version>2.10.2</version>
        </dependency>
        <dependency>
            <groupId>javax.annotation</groupId>
            <artifactId>javax.annotation-api</artifactId>
            <version>1.3.2</version>
        </dependency>
        <dependency>
            <groupId>org.nd4j</groupId>
            <artifactId>nd4j-api</artifactId>
            <version>1.0.0-beta7</version>
        </dependency>
        <dependency>
            <groupId>org.nd4j</groupId>
            <artifactId>nd4j-native</artifactId>
            <version>1.0.0-beta7</version>
        </dependency>
        <dependency>
            <groupId>org.deeplearning4j</groupId>
            <artifactId>deeplearning4j-core</artifactId>
            <version>1.0.0-beta7</version>
        </dependency>
        <dependency>
            <groupId>com.diffplug.matsim</groupId>
            <artifactId>matconsolectl</artifactId>
            <version>4.5.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-math3</artifactId>
            <version>3.6.1</version>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-sql_2.12</artifactId>
            <version>3.0.0</version>
            <exclusions>
                <exclusion>
                    <artifactId>janino</artifactId>
                    <groupId>org.codehaus.janino</groupId>
                </exclusion>
                <exclusion>
                    <artifactId>commons-compiler</artifactId>
                    <groupId>org.codehaus.janino</groupId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-core_2.12</artifactId>
            <version>3.0.0</version>
        </dependency>
        <dependency>
            <groupId>org.apache.spark</groupId>
            <artifactId>spark-mllib_2.12</artifactId>
            <version>3.0.0</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.apache.hadoop</groupId>
            <artifactId>hadoop-common</artifactId>
            <version>3.3.0</version>
        </dependency>
        <dependency>
            <groupId>org.codehaus.janino</groupId>
            <artifactId>janino</artifactId>
            <version>3.0.8</version>
        </dependency>
        <dependency>
            <groupId>org.codehaus.janino</groupId>
            <artifactId>commons-compiler</artifactId>
            <version>3.0.8</version>
        </dependency>
        <dependency>
            <groupId>org.ta4j</groupId>
            <artifactId>ta4j-core</artifactId>
            <version>0.13</version>
        </dependency>
```
