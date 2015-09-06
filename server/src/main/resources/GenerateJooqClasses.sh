MAVEN_PATH="$HOME/.m2/repository"

JOOQ_VERSION="3.5.4"
JOOQ_PATH="$MAVEN_PATH/org/jooq"
JOOQ="$JOOQ_PATH/jooq/$JOOQ_VERSION"
JOOQ_CODEGEN="$JOOQ_PATH/jooq-codegen/$JOOQ_VERSION"
JOOQ_META="$JOOQ_PATH/jooq-meta/$JOOQ_VERSION"

POSTGRES_VERSION="9.4-1201-jdbc41"
POSTGRES_PATH="$MAVEN_PATH/org/postgresql/postgresql/$POSTGRES_VERSION"

export CLASSPATH=.:\
$JOOQ/jooq-$JOOQ_VERSION.jar:\
$JOOQ_META/jooq-meta-$JOOQ_VERSION.jar:\
$JOOQ_CODEGEN/jooq-codegen-$JOOQ_VERSION.jar:\
$POSTGRES_PATH/postgresql-$POSTGRES_VERSION.jar

java org.jooq.util.GenerationTool JooqClassesGenerator.xml
