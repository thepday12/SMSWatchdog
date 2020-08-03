import 'package:flutterentradeapp/screens/login/LoginScreen.dart';
import 'package:flutterentradeapp/screens/Welcome.dart';
import 'package:flutterentradeapp/theme/theme.dart';
import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';
import 'package:provider/provider.dart';

import 'model/AuthModel.dart';

void main() => runApp(MyApp());

class MyApp extends StatelessWidget {
  final wordPair = WordPair.random();

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider<Auth>(
        create: (context) => Auth(),
        child: MaterialApp(
          title: 'Provider Demo',
          theme: appTheme,
          initialRoute: '/',
          routes: {
            '/': (context) => LoginScreen(),
            '/welcome': (context) => WelcomeScreen(),
          },
        ));
  }
}
