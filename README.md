Swing Components
================
Swing-Components is an open-source library that extends Swing framework components with extra features.

[![Build Status](https://github.com/squdan/swing-components/workflows/querydsl-filters/badge.svg)](https://github.com/squdan/swing-components/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.squdan/swing-components/badge.svg?style=flat-square&color=007ec6)](https://maven-badges.herokuapp.com/maven-central/io.github.squdan/swing-components/)

## Requirements

1. Java 17+
2. Maven 3.9.5+

## Uses

1. [Lombok 1.18.30+](https://github.com/projectlombok/lombok) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.projectlombok/lombok/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.projectlombok/lombok/)
2. [Querydsl-filters 0.0.1+](https://github.com/squdan/querydsl-filters) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/io.github.squdan/querydsl-filters/badge.svg?style=flat-square&color=007ec6)](https://maven-badges.herokuapp.com/maven-central/io.github.squdan/querydsl-filters/)
3. [Flatlaf 3.2.5+](https://github.com/JFormDesigner/FlatLaf) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.formdev/flatlaf/badge.svg?style=flat-square&color=007ec6)](https://maven-badges.herokuapp.com/maven-central/com.formdev/flatlaf)
4. [SwingX 1.6.1+](https://github.com/arotenberg/swingx?tab=readme-ov-file) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.swinglabs/swingx/badge.svg?style=flat-square&color=007ec6)](https://maven-badges.herokuapp.com/maven-central/org.swinglabs/swingx)
5. [LGoodDatePicker 11.2.1+](https://github.com/LGoodDatePicker/LGoodDatePicker) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.lgooddatepicker/LGoodDatePicker/badge.svg?style=flat-square&color=007ec6)](https://maven-badges.herokuapp.com/maven-central/com.github.lgooddatepicker/LGoodDatePicker)

## Getting started

### Setting up the dependency

The first step is to include Swing-Components into your project. You can download Maven dependency from
**Maven Central**.

```maven
<dependency>
    <groupId>io.github.squdan</groupId>
    <artifactId>swing-components</artifactId>
    <version>0.0.1</version>
</dependency>
```

## Project configuration

Once you have the dependency into your project, it requires a minimum configuration to work into your project.
SwingComponents dependency requires color and fonts configuration, so you need to setup it before call any of their
components.

```java
package io.github.squdan.swing.components.examples;

import io.github.squdan.swing.components.FlatLafFontText;
import io.github.squdan.swing.components.FontText;
import io.github.squdan.swing.components.FlatLafFontTitles;
import io.github.squdan.swing.components.configuration.SwingComponents;
import io.github.squdan.swing.components.configuration.SwingComponentsConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.swing.*;

@SpringBootApplication
public class ExampleApplication {

    public static void main(String[] args) {
        // Styles configuration
        configureAppUI();

        // Configuring spring app launch with headless false o avoid HeadlessException
        // using Swing
        SpringApplicationBuilder builder = new SpringApplicationBuilder(PodiclinicDesktopApplication.class);
        builder.headless(false);
        builder.run(args);
    }

    public static void configureAppUI() {
        // Enable custom window decorations
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        // SwingComponents dependency configuration
        SwingComponents.initFlatLaf();
        SwingComponents.setup(SwingComponentsConfiguration.builder()
                .textConfiguration(SwingComponentsConfiguration.SwingComponentsTextConfiguration.builder()
                        .titleFont(FlatLafFontTitles.FONT_TITLE_1.getFont())
                        .titleSecondaryFont(FlatLafFontTitles.FONT_TITLE_2.getFont())
                        .defaultText(FlatLafFontText.FONT_DEFAULT.getFont())
                        .build())
                .colorConfiguration(SwingComponentsConfiguration.SwingComponentsColorConfiguration.builder()
                        .primary(ApplicationColors.PRIMARY_COLOR.getColor())
                        .primaryText(ApplicationColors.PRIMARY_TEXT_COLOR.getColor())
                        .secondary(ApplicationColors.PRIMARY_COLOR.getColor())
                        .secondaryText(ApplicationColors.PRIMARY_TEXT_COLOR.getColor())
                        .success(ApplicationColors.GREEN.getColor())
                        .danger(ApplicationColors.RED_LIGTH.getColor())
                        .warning(ApplicationColors.YELLOW_LIGTH.getColor())
                        .build())
                .build());
    }
}

```

## Bugs and Feedback

For bugs, questions and discussions please use the [Github Issues](https://github.com/squdan/swing-components/issues).

## Communication

- Email: i02torod@gmail.com
