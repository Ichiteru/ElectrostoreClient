package com.chern.clientapplication.controller;

import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FxmlView("user-page.fxml")
public class UserPageController extends Controller {
}
