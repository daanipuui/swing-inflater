# swing-inflater
Xml Inflater for Java Swing

## Example

### Maven dependency

```
<dependency>
    <groupId>com.danielpuiu</groupId>
    <artifactId>swing-inflater</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

### Java code

```
public class SwingApplication {

    public static void main(String[] args) {
        InputStream inputStream = SwingApplication.class.getClassLoader().getResourceAsStream("layout.xml");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);

            ComponentLoader loader = new ComponentLoader();
            JPanel container = loader.load(inputStream);
            frame.getContentPane().add(container);

            frame.pack();
            frame.setVisible(true);
        });
    }
}

```

### Xml file

```
<?xml version="1.0"?>
<JPanel>

    <BorderLayout/>

    <JButton
        name="north"
        text="north"
        layout:position="NORTH"/>

    <JButton
        name="south"
        text="south"
        layout:position="SOUTH"/>

    <JButton
        name="center"
        text="center"/>

    <JButton
        name="east"
        text="east"
        layout:position="EAST"/>

    <JButton
        name="west"
        text="west"
        layout:position="BorderLayout.WEST"/>

</JPanel>
```

### Xml Conventions

1. The layout is defined in the component. It can have its own attributes depending on the layout's type.
2. The component's attributes (`name`, `text`, etc) are set by calling the corresponding setter method (`setName`, `setText`, etc).
3. In order to define layout constraints a layout constraints converter must be registered for the layout used. 
Most of Swing layouts are already defined, except GroupLayout.
4. One can define its own layout constraint converter along with needed type converters.

### BorderLayout constraints

The only constraint is the `position`: `NORTH`, `SOUTH`, `EAST`, `WEST`, `CENTER`, etc.
Because reflection is used, any future constant is automatically supported.

```
layout:position="BorderLayout.WEST"
```

or 

```
layout:position="WEST"
```

### CardLayout constraints

The only constraint is the id of the card: 

```
layout:cardId="First"
```

### FlowLayout constraints

Flow layout doesn't not define any constraint.
But, we can still define attributes for the layout such as the alignment.

```
<?xml version="1.0"?>
<JPanel>

    <FlowLayout
        align="LEFT"/>

    <JButton
        name="north"
        text="north"/>

    <JButton
        name="south"
        text="south"/>

    <JButton
        name="center"
        text="center"/>

    <JButton
        name="east"
        text="east"/>

    <JButton
        name="west"
        text="west"/>

</JPanel>
```

### GridBagLayout constraints

All the defined constraints are available: `fill`, `gridx`, `gridy`, `gridwidth`, `gridheight`, `ipadx`, `ipady`, 
`anchor`, `insets`, `weightx` and `weighty`.

```
<JPanel>

    <GridBagLayout/>

    <JButton
        name="Button 1"
        text="Button 1"
        layout:fill="HORIZONTAL"
        layout:gridx="0"
        layout:gridy="0"
        layout:weightx="0.5"/>
        
...
</JPanel>
```

### GridLayout constraints

Grid layout doesn't not define any constraint.
Of course, the attributes of the layout can still be defined.

```
<?xml version="1.0"?>
<JPanel>

    <GridLayout
        rows="0"
        columns="2"/>

...
</JPanel>
```

### SpringLayout constraints

In order to make it more simple to use, the constraints have been improved.
The following constraints can be used: `above`, `alignBottom`, `alignLeft`, `alignRight`, `alignTop`, `below`, 
`centerHorizontal`, `centerVertical`, `leftOf` and `rightOf`.

They all must be used against a component name and a pad size.

```
<JPanel
    name="#contentPanel"/>
    
    <SprintLayout/>
    
    <JButton
        name="#button"
        layout:alignRight="#contentPanel, -100"/>
        
...
</JPanel>
```

This corresponds to the following SprintLayout constraint:
```
layout.putConstraint(WEST, button, -100, WEST, contentPanel);
```

The pad size can be ignored as follows, in which case is considered `0`:
```
layout:alignRight="#contentPanel"
```

The following shows the relations between the xml constraints and the spring layout constraints:

Xml Constraint | Java Constraint
--- | ---
above | layout.putConstraint(**SOUTH**, component1, pad, **NORTH**, component2)
alignBottom | layout.putConstraint(**SOUTH**, component1, pad, **SOUTH**, component2)
alignLeft | layout.putConstraint(**WEST**, component1, pad, **WEST**, component2)
alignRight | layout.putConstraint(**EAST**, component1, pad, **EAST**, component2)
alignTop | layout.putConstraint(**NORTH**, component1, pad, **NORTH**, component2)
below | layout.putConstraint(**NORTH**, component1, pad, **SOUTH**, component2)
centerHorizontal | layout.putConstraint(**HORIZONTAL_CENTER**, component1, pad, **HORIZONTAL_CENTER**, component2)
centerVertical | layout.putConstraint(**VERTICAL_CENTER**, component1, pad, **VERTICAL_CENTER**, component2)
leftOf | layout.putConstraint(**EAST**, component1, pad, **WEST**, component2)
rightOf | layout.putConstraint(**WEST**, component1, pad, **EAST**, component2)

## Developed By

Daniel PUIU

## License

Copyright 2019 Daniel PUIU

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
