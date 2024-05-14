# LoginKotlin
登录界面（手机号+账号）设计


基于Kotlin语言，利用MVI架构View和ViewModel的单向ViewState数据流（解决MVVM中双向绑定数据的不足，把双向绑定变成单向数据流），通过DataBinding完成数据和xml绑定。

以LoginPhoneActivity页面为例，当用户输入手机号或者密码（影响登录按钮是否高亮、下划线颜色、图标样式等）会影响界面显示，
从而提炼出LoginPhoneVo类将数据（手机号文本+密码文本，数据对界面的影响聚焦到LoginPhoneVo）转化对应的界面状态， 通过DataBinding完成将该状态绑定到xml对应的控
件上，从而完 成界面的更新。（同理如果LoginPhoneVM网络请求返回的数据需要更新到界面，也是转化为对应状态，绑定到xml上）