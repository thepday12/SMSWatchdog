import 'package:entrade/theme/theme.dart';
import 'package:flutter/material.dart';

Widget inputDecorationFromResource(String source) {
  return Image(image:AssetImage(source),height:20 ,width: 20, fit: BoxFit.fill);
}

Widget inputDecorationIcon(IconData source) {
  return Icon(source,size: 24,color: appTheme.primaryColor);
}


fieldFocusChange(BuildContext context,FocusNode nextFocus) {
  FocusScope.of(context).requestFocus(nextFocus);
}