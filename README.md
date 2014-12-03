jtwig-data-grid
===============
This library provides addons for the [Jtwig template engine](http://jtwig.org/) to build data grids. It is based on the [data-grid project](https://github.com/customweb/data-grid). It provides an easy integration with the Spring framework.

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
| var           | true      |             | Name of the variable that contains the current row's data. |
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

| Attribute       | Required  | Default     | Description |
| --------------- | --------- | ----------- | ----------- |
| title           | false     |             | Column's title. Will be used in the table's header row. |
| fieldName       | false     |             | Name of the field for data binding. |
| sortable        | false     | false       | Make the column sortable. This is an empty attribute. |
| filterable      | false     | false       | Make the column filterable. This is an empty attribute. |
| filterOptions   | false     |             | A key - value map that contains options that are used for filtering. |
| filterOperator  | false     | Type specific | Define how the values should be filtered. Possible values: `>`, `<`, `=`, `contains`. |
| labelForTrue    | false     | True        | Label for the value `true` (used as filter option for boolean values). |
| labelForFalse    | false    | False        | Label for the value `false` (used as filter option for boolean values). |

#### Filter ####
Renders an HTML input field to filter a grid column.

```twig
{% grid:grid %}
  {% grid:filter fieldName="..." %}{% endgrid:filter %}
{% endgrid:grid %}
```

Must be defined inside of a `grid:grid` tag.

Dynamic attributes are allowed.

| Attribute       | Required  | Default     | Description |
| --------------- | --------- | ----------- | ----------- |
| fieldName       | true      |             | Name of the field for data binding. |
| defaultOperator | false     | Type specific | Define how the values should be filtered. Possible values: `>`, `<`, `=`, `contains`. |
| showOperator    | false     |             | Show the operator. This is an empty attribute. |

#### Limit ####
Renders an HTML select field to select the number of rows on a page.

```twig
{% grid:grid %}
  {% grid:limit steps="..." %}{% endgrid:limit %}
{% endgrid:grid %}
```

Must be defined inside of a `grid:grid` tag.

Dynamic attributes are allowed.

| Attribute       | Required  | Default     | Description |
| --------------- | --------- | ----------- | ----------- |
| steps           | true      |             | Comma-separated numbers that define the possible page sizes. |

#### Pager ####
Renders the page navigation for the grid.

```twig
{% grid:grid %}
  {% grid:pager %}{% endgrid:pager %}
{% endgrid:grid %}
```

Must be defined inside of a `grid:grid` tag.

Dynamic attributes are allowed.

| Attribute           | Required  | Default     | Description |
| ------------------- | --------- | ----------- | ----------- |
| maxPageItems        | false     |             | Number of pages to list. |
| showPreviousButton  | false     |             | Show the button to navigate to the previous page. This is an empty attribute. |
| showNextButton  | false     |             | Show the button to navigate to the next page. This is an empty attribute. |
| showFirstButton  | false     |             | Show the button to navigate to the first page. This is an empty attribute. |
| showLastButton  | false     |             | Show the button to navigate to the last page. This is an empty attribute. |

#### Submit ####
Renders an HTML button to update the grid.

```twig
{% grid:grid %}
  {% grid:submit %}
    ...
  {% endgrid:submit %}
{% endgrid:grid %}
```

Must be defined inside of a `grid:grid` tag.

The tag's content is used as label for the HTML 'button' element.

Dynamic attributes are allowed.

### Templates ###
Each element has it's own template. By defining a custom resource resolver, you can override those and use your own templates.

First, write an implementation by extending `com.customweb.jtwig.grid.model.AbstractResourceResolver`.

```java
public class MyResourceResolver extends AbstractResourceResolver {
	@Override
	public JtwigResource resolve(String resourceName) throws ResourceException {
		...
	}
}
```

Then, register the resolver by calling it's register method.

### Spring Integration ###
To use the grid tags, they have to be registered first.

```xml
<bean id="viewResolver" class="com.lyncode.jtwig.mvc.JtwigViewResolver">
	<property name="prefix" value="/WEB-INF/views/" />
	<property name="suffix" value=".twig" />
</bean>
<bean class="com.customweb.jtwig.grid.spring.ViewResolverAddon" />
```

#### Templates ####
To use a custom resource resolver, it can be registered in the spring configuration:

```xml
<bean class="my.package.MyResourceResolver" init-method="register"/>
```
