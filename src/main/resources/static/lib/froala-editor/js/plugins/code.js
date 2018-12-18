/*!
 * froala_editor v2.9.1 (https://www.froala.com/wysiwyg-editor)
 * License https://froala.com/wysiwyg-editor/terms/
 * Copyright 2014-2018 Froala Labs
 */
(function ($) {
    // Add an option for your plugin.
    $.FroalaEditor.DEFAULTS = $.extend($.FroalaEditor.DEFAULTS, {
        insertCodeOption: {
            default: 'htmlmixed',
            languages: [
                'css', 'erlang', 'go', 'groovy', 'htmlmixed', 'javascript', 'lua', 'markdown', 'nginx', 'php',
                'powershell', 'python', 'ruby', 'sass', 'shell', 'sql', 'stylus', 'swift', 'vb', 'vbscript',
                'xml', 'yaml'
            ]
        }
    });

    // Define the plugin.
    // The editor parameter is the current instance.
    $.FroalaEditor.PLUGINS.insertCode = function (editor) {
        // Private variable visible only inside the plugin scope.
        var insertCodeWindow, insertCodeHead, insertCodeBody, pluginName = 'insert_code', codeMirrorEditor;

        // Private method that is visible only inside plugin scope.
        function _privateMethod() {
            console.log(pluginName);
        }

        // Public method that is visible in the instance scope.
        function show() {
            if (!insertCodeWindow) {
                var titleHtml = "<h4>" + editor.language.translate("Insert Code") + "</h4>",
                    contentHtml = "<textarea id='insertCodeContent' placeholder='输入代码'></textarea>",
                    //create modal window
                    modal = editor.modals.create(pluginName, titleHtml, contentHtml);
                insertCodeWindow = modal.$modal;
                insertCodeHead = modal.$head;
                insertCodeBody = modal.$body;
                insertCodeHead.append(function () {
                    var select = [];
                    select.push('<select class="choose-codemirror-language" style="height: 30px;margin: 6px 0;float: left;display: inline-block">');
                    for (var i = 0; i < editor.opts.insertCodeOption.languages.length; i++) {
                        var lan = editor.opts.insertCodeOption.languages[i];
                        if (lan === editor.opts.insertCodeOption.default) {
                            select.push('<option value="' + lan + '" selected>' + lan + '</option>');
                        } else {
                            select.push('<option value="' + lan + '">' + lan + '</option>');
                        }
                    }
                    select.push('</select>');
                    return select.join('');
                });
                insertCodeHead.css({
                    'z-index': 100
                });
                insertCodeBody.css({
                    textAlign: 'left',
                    overflow: 'auto',
                    paddingBottom: 0
                });
                insertCodeBody.append('<div style="display: block;text-align: right;line-height: 42px">' +
                    '<button class="fr-command insert">插入</button>' +
                    '<button class="fr-command cancel" style="margin:0 12px;color: #888">取消</button>' +
                    '</div>');
                editor.events.$on($(editor.o_win), "resize", function () {
                    (insertCodeWindow.data("instance") || editor).modals.resize(pluginName)
                });
                editor.events.bindClick(insertCodeBody, ".insert", function (e) {
                    var id = 'code-' + new Date().getTime();
                    editor.insertCode.hide();
                    editor.undo.saveStep();
                    editor.html.insert('<pre id="' + id + '"></pre>', true);
                    editor.undo.saveStep();
                    //todo 待处理
                    CodeMirror(document.getElementById(id), {
                        value: codeMirrorEditor.getValue(),
                        mode: editor.opts.insertCodeOption.default,     // 默认模式
                        indentUnit: 4,                          // 缩进单位为4
                        lineNumbers: true,                      // 显示行数
                        styleActiveLine: true,                  // 当前行背景高亮
                        matchBrackets: true,                    // 括号匹配
                        lineWrapping: true                      // 自动换行
                    });
                });
                editor.events.bindClick(insertCodeBody, ".cancel", function () {
                    (insertCodeWindow.data("instance") || editor).insertCode.hide();
                }, false);
                editor.events.$on(insertCodeHead, 'change', function (e) {
                    codeMirrorEditor.setOption("mode", e.target.value);
                });
            }
            editor.modals.show(pluginName);
            editor.modals.resize(pluginName);
            if (!codeMirrorEditor) {
                codeMirrorEditor = _initCodeMirrorEditor(insertCodeBody.find('#insertCodeContent').get(0));
            } else {
                codeMirrorEditor.setValue('');
            }
        }

        function _initCodeMirrorEditor(codeElement) {
            var codeEditor = CodeMirror.fromTextArea(codeElement, {
                mode: editor.opts.insertCodeOption.default,     // 默认模式
                indentUnit: 4,                          // 缩进单位为4
                lineNumbers: true,                      // 显示行数
                styleActiveLine: true,                  // 当前行背景高亮
                matchBrackets: true,                    // 括号匹配
                lineWrapping: true                      // 自动换行
            });

            codeEditor.setOption("extraKeys", {
                // Tab键换成4个空格
                Tab: function (cm) {
                    var spaces = Array(cm.getOption("indentUnit") + 1).join(" ");
                    cm.replaceSelection(spaces);
                },
                // F11键切换全屏
                "F11": function (cm) {
                    cm.setOption("fullScreen", !cm.getOption("fullScreen"));
                },
                // Esc键退出全屏
                "Esc": function (cm) {
                    if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
                }
            });
            return codeEditor;
        }

        function hide() {
            editor.modals.hide(pluginName)
        }

        // The start point for your plugin.
        function _init() {
            // You can access any option from documentation or your custom options.

            // Call any method from documentation.
            // editor.methodName(params);

            // You can listen to any event from documentation.
            // editor.events.add('contentChanged', function (params) {});
        }

        // Expose public methods. If _init is not public then the plugin won't be initialized.
        // Public method can be accessed through the editor API:
        // $('.selector').froalaEditor('insertCode.publicMethod');
        return {
            _init: _init,
            show: show,
            hide: hide
        }
    };

    // Define an icon.
    $.FroalaEditor.DefineIcon('insertCode', {NAME: 'file-code-o'});

    // Define a button.
    $.FroalaEditor.RegisterCommand('insertCode', {
        // Button title.
        title: 'Insert Code',

        // Specify the icon for the button.
        // If this option is not specified, the button name will be used.
        icon: 'insertCode',

        // Save the button action into undo stack.
        undo: false,

        // Focus inside the editor before the callback.
        focus: false,

        // Show the button on mobile or not.
        showOnMobile: false,

        modal: false,

        // Refresh the buttons state after the callback.
        refreshAfterCallback: false,

        // Called when the button is hit.
        callback: function () {
            // The current context is the editor instance.
            this.insertCode.show();
        },

        plugin: 'insertCode'
    })
})(jQuery);