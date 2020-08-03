import 'package:flutterentradeapp/model/AuthModel.dart';
import 'package:flutter/material.dart';
import 'package:provider/provider.dart';

class WelcomeScreen extends StatefulWidget {
  @override
  State<StatefulWidget> createState() {
    return _WelcomeScreen();
  }
}

class _WelcomeScreen extends State<WelcomeScreen> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(body: Consumer<Auth>(builder: (context, auth, child) {
      return new Container(
          margin: new EdgeInsets.symmetric(horizontal: 16),
          child: Center(
              child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                Text("Welcome2"),
                SizedBox(
                  height: 16,
                ),
                Text(auth.token)
              ])));
    }));
  }
}
