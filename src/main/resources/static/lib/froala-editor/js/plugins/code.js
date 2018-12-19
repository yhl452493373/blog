/*!
 * froala_editor v2.9.1 (https://www.froala.com/wysiwyg-editor)
 * License https://froala.com/wysiwyg-editor/terms/
 * Copyright 2014-2018 Froala Labs
 */
(function ($) {
    // Add an option for your plugin.
    $.FroalaEditor.DEFAULTS = $.extend($.FroalaEditor.DEFAULTS, {
        insertCodeOption: {
            default: 'html',
            languages: [
                'coffee', 'csharp', 'css', 'dart', 'erlang', 'ftl', 'golang', 'groovy', 'html', 'jade', 'java',
                'javascript', 'json', 'jsp', 'kotlin', 'less', 'lua', 'markdown', 'mysql', 'objectivec', 'php',
                'perl', 'perl6', 'php', 'python', 'ruby', 'sass', 'sql', 'stylus', 'swift', 'text',
                'typescript', 'vbscript', 'xml', 'yaml'
            ]
        }
    });

    // Define the plugin.
    // The editor parameter is the current instance.
    $.FroalaEditor.PLUGINS.insertCode = function (editor) {
        // Private variable visible only inside the plugin scope.
        var insertCodeWindow, insertCodeHead, insertCodeBody, pluginName = 'insert_code', codeEditor;

        // Public method that is visible in the instance scope.
        function show() {
            if (!insertCodeWindow) {
                var titleHtml = "<h4>" + editor.language.translate("Insert Code") + "</h4>",
                    contentHtml = "<pre id='insertCodeContent' style='height: 350px'></pre>",
                    //create modal window
                    modal = editor.modals.create(pluginName, titleHtml, contentHtml);
                insertCodeWindow = modal.$modal;
                insertCodeHead = modal.$head;
                insertCodeBody = modal.$body;
                insertCodeHead.append(function () {
                    var select = [];
                    select.push('<select class="choose-code-language" style="height: 30px;margin: 6px 0;float: left;display: inline-block">');
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
                    //todo 判断是否在代码块中.
                    editor.insertCode.hide();
                    editor.undo.saveStep();
                    editor.html.insert('<pre class="ace_code" contenteditable="false" ace-mode="ace/mode/' + insertCodeHead.find('.choose-code-language').val() + '" ace-theme="ace/theme/tomorrow" ace-gutter="true">' + codeEditor.getValue().replace(/</gm,'&lt;').replace(/>/gm,'&gt;') + '</pre><p></p>', true);
                    editor.undo.saveStep();
                    _staticHighlight($('.ace_code'))
                });
                editor.events.bindClick(insertCodeBody, ".cancel", function () {
                    (insertCodeWindow.data("instance") || editor).insertCode.hide();
                }, false);
                editor.events.$on(insertCodeHead, 'change', function (e) {
                    codeEditor.session.setMode("ace/mode/" + e.target.value);
                });
            }
            editor.modals.show(pluginName);
            editor.modals.resize(pluginName);
            if (!codeEditor) {
                codeEditor = _initCodeEditor('insertCodeContent');
            } else {
                codeEditor.setValue('');
            }
        }

        function _initCodeEditor(codeEditorElementId) {
            ace.require("ace/ext/language_tools");
            var tempCodeEditor = ace.edit(codeEditorElementId);
            tempCodeEditor.session.setMode("ace/mode/html");
            tempCodeEditor.setTheme("ace/theme/tomorrow");
            tempCodeEditor.setOptions({
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });
            return tempCodeEditor;
        }

        function _staticHighlight($elements) {
            ace.require("ace/lib/dom");
            var highlight = ace.require("ace/ext/static_highlight");
            $elements.each(function () {
                highlight(this, {
                    mode: this.getAttribute("ace-mode"),
                    theme: this.getAttribute("ace-theme"),
                    startLineNumber: 1,
                    showGutter: this.getAttribute("ace-gutter"),
                    trim: true
                }, function (highlighted) {

                });
            });
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