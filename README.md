jtwig-data-grid
===============
This library provides addons for the [Jtwig template engine](http://jtwig.org/) to build data grids. It is based on the (data-grid project)[https://github.com/customweb/data-grid]. It provides an easy integration with the Spring framework.

## Installation ##
The project contains a Maven .pom file to include all depencendies.

## Usage ##
To use the form tags, they have to be registered first.

```java
JtwigConfiguration config = new JtwigConfiguration();
GridAddon.addons(config);
JtwigTemplate template = new JtwigTemplate(..., config);
```

### Tags ###
All grid tags must have an opening (prefix `grid:`) and closig tag (prefix `endgrid:`).

#### Grid ####
Renders a 'grid' tag and exposes a binding path to inner tags for binding.

```twig
{% grid:grid %}
  ...
{% endgrid:grid %}
```

Dynamic attributes are allowed.

| Attribute   | Required  | Default     | Description |
| ----------- | --------- | ----------- | ----------- |
| ajax        | false     | false       | Use ajax calls to update the grid instead of reloading the page. |
| model       | false     | gridModel   | Name of the JtwigModelMap entry under which the grid object is exposed. |

#### Table ####
Renders an HTML table containing the grid model's data.

```twig
{% grid:grid %}
  {% grid:table var="..." %}
    ...
  {% endgrid:table %}
{% endgrid:grid %}
```

Must be defined inside of a `grid:grid` tag.

Dynamic attributes are allowed.

| Attribute     | Required  | Default     | Description |
| ------------- | --------- | ----------- | ----------- |
| var           | true      |             | Define the name of the variable that contains the current row's data. |
| showFilterRow | false     | false       | Show the filter input fields beneath the header row. |

#### Column ####
Defines a table column to be rendered.

```twig
{% grid:grid %}
  {% grid:table var="..." %}
    {% grid:column %}
      ...
    {% endgrid:column %}
  {% endgrid:table %}
{% endgrid:grid %}
```

Must be defined inside of a `grid:table` tag.

Dynamic attributes are allowed.

| Attribute     | Required  | Default     | Description |
| ------------- | --------- | ----------- | ----------- |
| title         | false     |             | Define the column's title. Will be used in the table's header row. |
| fieldName     | false     |             | Name of the field for data binding.  |
| sortable      | false     | false       | Setting this attribute (withou a value) will make the column sortable. |
| filterable    | false     | false       | Setting this attribute (withou a value) will make the column filterable. |
| filterOptions | false     |             | Define a key - value map that contains options that are used for filtering. |
