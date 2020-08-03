import 'package:flutter/material.dart';

import 'HexColor.dart';

final appTheme = ThemeData(
  primaryColor: HexColor("#800000"),
  textTheme: TextTheme(
    headline2: TextStyle(
      fontFamily: 'Roboto',
      fontWeight: FontWeight.w700,
      fontSize: 24,
      color: Colors.black,
    ),
  ),
);