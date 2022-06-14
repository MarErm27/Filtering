# Filtering

Filtering is a Scala GraphQL investigation project called upon to find out how to properly filter queries in Scala GraphQL.

## Libraries

Play Framework: <https://www.playframework.com/documentation/2.8.x/Home>

Play JSON: <https://www.playframework.com/documentation/2.8.x/ScalaJson>

Dependency Injection with Guice: <https://www.playframework.com/documentation/2.8.x/ScalaDependencyInjection>

Slick: <https://scala-slick.org/doc/3.3.3/>

Sangria (GraphQL): <https://sangria-graphql.github.io/learn/>
#
## Running

The following command will start up Play in development mode:

```bash
sbt "~run 9990"
```

## Creating tables

This project uses MySQL database driver compatible with [Percona](https://www.percona.com/) database. To create the test tables, run the script below.

```
CREATE TABLE `user` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`email` VARCHAR(255)  NULL  DEFAULT NULL,
`password` VARCHAR(255)  NULL  DEFAULT NULL,
`verified` BOOLEAN      NOT NULL DEFAULT 0,
`birthdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`created_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6),
`updated_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
`is_deleted`     BOOLEAN      NOT NULL DEFAULT 0,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `order` (
`id` int(11) unsigned NOT NULL AUTO_INCREMENT,
`name` VARCHAR(255)  NULL  DEFAULT NULL,
`user_id` int(11) unsigned NULL  DEFAULT NULL,
`expected_delivery_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`created_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6),
`updated_at`  TIMESTAMP(6)     NULL     DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
`is_deleted`     BOOLEAN      NOT NULL DEFAULT 0,
PRIMARY KEY (`id`),
KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
```

## Running queries

The first query for testing purposes:
```
query  {
  ordersCounts(limit: 5, offset: 0, date: "1990-01-01", filters: {
    id: "name",
    like: "yellow"
  }) {
    ordersList{
      id,
      name,
      userId,
      expectedDeliveryDate,
      createdAt,
      updatedAt,
      isDeleted,
      userInfo{
        id,
        email,
        verified,
        birthdate,
        createdAt,
        updatedAt,
        isDeleted
      }
    }
    count
  }
}
```

## The goal
The goal is to find a generalized way to filter GraphQL queries and keep the GraphQL fetchers working effectively, i.e. fetchers have to apply filters before executing database queries.

The example above "ordersCounts" contains parameter "filters" - this is an array of optional filters (like, gt, lt, in), where `gr` (greater than) and `lt` (lesser than) might be used for date, int and string comparison, `like` is for strings and `in` for finding values in set.