/*!
 * froala_editor v2.9.1 (https://www.froala.com/wysiwyg-editor)
 * License https://froala.com/wysiwyg-editor/terms/
 * Copyright 2014-2018 Froala Labs
 */
(function ($) {
    var defaultLanguageList = [
        'coffee', 'csharp', 'css', 'dart', 'erlang', 'ftl', 'golang', 'groovy', 'html', 'jade', 'java',
        'javascript', 'json', 'jsp', 'kotlin', 'less', 'lua', 'markdown', 'mysql', 'objectivec', 'php',
        'perl', 'perl6', 'php', 'python', 'ruby', 'sass', 'sql', 'stylus', 'swift', 'text',
        'typescript', 'vbscript', 'xml', 'yaml'
    ];
    var defaultLanguage = defaultLanguageList[0];
    $.FroalaEditor.DEFAULTS = $.extend($.FroalaEditor.DEFAULTS, {
        insertCodeOption: {
            default: defaultLanguage,
            languages: defaultLanguageList
        }
    });

    $.extend($.FroalaEditor.POPUP_TEMPLATES, {
        // 若使用此行,则弹窗需要设置弹窗内容: custom_layer: '<div class="custom-layer">Hello World!</div>'
        // "insertCode.popup": '[_BUTTONS_][_CUSTOM_LAYER_]'
        "insertCode.popup": '[_BUTTONS_]'
    });


    $.FroalaEditor.PLUGINS.insertCode = function (editor) {
        var insertButtonName = editor.language.translate("Insert");
        var updateButtonName = editor.language.translate("Update");
        var cancelButtonName = editor.language.translate("Cancel");

        var insertCodeWindow, insertCodeHead, insertCodeBody, pluginName = 'insert_code', codeEditor, highlightBlock;

        function _highlightBlock() {
            return highlightBlock;
        }

        function show(content) {
            if (!insertCodeWindow) {
                var languageList = editor.opts.insertCodeOption.languages || defaultLanguageList;
                var defaultLan = editor.opts.insertCodeOption.default || defaultLanguage;
                var titleHtml = "<h4>" + editor.language.translate("Insert Code") + "</h4>",
                    contentHtml = "<pre id='insertCodeContent' style='height: 350px'>" + (content || "") + "</pre>",
                    //create modal window
                    modal = editor.modals.create(pluginName, titleHtml, contentHtml);
                insertCodeWindow = modal.$modal;
                insertCodeHead = modal.$head;
                insertCodeBody = modal.$body;
                insertCodeHead.append(function () {
                    var select = [];
                    select.push('<select class="choose-code-language" style="height: 30px;margin: 6px 0;float: left;display: inline-block">');
                    for (var i = 0; i < languageList.length; i++) {
                        var lan = languageList[i];
                        if (defaultLan == null && i === 0) {
                            select.push('<option value="' + lan + '" selected>' + lan + '</option>');
                        } else {
                            if (lan === defaultLan) {
                                select.push('<option value="' + lan + '" selected>' + lan + '</option>');
                            } else {
                                select.push('<option value="' + lan + '">' + lan + '</option>');
                            }
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
                    '<button class="fr-command insert">' + insertButtonName + '</button>' +
                    '<button class="fr-command cancel" style="margin:0 12px;color: #888">' + cancelButtonName + '</button>' +
                    '</div>');
                editor.events.$on($(editor.o_win), "resize", function () {
                    (insertCodeWindow.data("instance") || editor).modals.resize(pluginName)
                });
                editor.events.bindClick(insertCodeBody, ".insert", function (e) {
                    editor.insertCode.hide();
                    editor.undo.saveStep();
                    if (highlightBlock) {
                        highlightBlock.replaceWith('<pre contenteditable="false" class="ace_code_highlight" ace-mode="ace/mode/' + insertCodeHead.find('.choose-code-language').val() + '" ace-theme="ace/theme/tomorrow" ace-gutter="true">' + codeEditor.getValue().replace(/</gm, '&lt;').replace(/>/gm, '&gt;') + '</pre><p></p>');
                    } else {
                        editor.html.insert('<pre contenteditable="false" class="ace_code_highlight" ace-mode="ace/mode/' + insertCodeHead.find('.choose-code-language').val() + '" ace-theme="ace/theme/tomorrow" ace-gutter="true">' + codeEditor.getValue().replace(/</gm, '&lt;').replace(/>/gm, '&gt;') + '</pre><p></p>', true);
                    }
                    editor.undo.saveStep();
                    _staticHighlight($('.ace_code_highlight'));
                    highlightBlock = null;
                });
                editor.events.bindClick(insertCodeBody, ".cancel", function () {
                    (insertCodeWindow.data("instance") || editor).insertCode.hide();
                }, false);
                editor.events.$on(insertCodeHead, 'change', function (e) {
                    codeEditor.session.setMode("ace/mode/" + e.target.value);
                });
            }
            if (!codeEditor) {
                codeEditor = _initCodeEditor('insertCodeContent', defaultLan);
            } else {
                if (!content) {
                    codeEditor.setValue('');
                } else {
                    //-1表示到第一行,1表示到最后一行
                    codeEditor.setValue(content, 1);
                }
            }
            var tempBody = editor.modals.get(pluginName).$body;
            if (content) {
                tempBody.find('.insert').text(updateButtonName);
            } else {
                tempBody.find('.insert').text(insertButtonName);
            }
            editor.modals.show(pluginName);
            editor.modals.resize(pluginName);
        }

        function hide() {
            editor.modals.hide(pluginName)
        }

        function _showToolPopup(event) {
            if (editor.core.hasFocus() && event) {
                var $target = $(event.target);
                if ($target.closest('.ace_code_highlight').length === 1 && $target.closest('.fr-view').length === 1) {
                    highlightBlock = $target.closest('.ace_code_highlight');
                    _showPopup();
                } else {
                    highlightBlock = null;
                }
            }
        }

        function _initCodeEditor(codeEditorElementId, defaultLan) {
            ace.require("ace/ext/language_tools");
            var tempCodeEditor = ace.edit(codeEditorElementId);
            tempCodeEditor.session.setMode("ace/mode/" + defaultLan);
            tempCodeEditor.setTheme("ace/theme/tomorrow");
            tempCodeEditor.setOptions({
                autoScrollEditorIntoView: true,
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
                if ($(this).find('.ace_static_highlight').length === 0)
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

        function _init() {
            editor.events.on("window.mouseup", _showToolPopup);
            editor.events.$on(editor.$wp, "scroll.insertCode-popup", function (e) {
                editor.popups.isVisible("insertCode.popup") && _showPopup();
            })
        }

        // Create custom popup.
        function _initPopup() {
            // Popup buttons.
            var popup_buttons = '';

            // Create the list of buttons.
            popup_buttons += '<div class="fr-buttons">';
            popup_buttons += editor.button.buildList(['codeRemove', 'codeEdit']);
            popup_buttons += '</div>';

            // 指定弹出层内容
            var template = {
                buttons: popup_buttons
            };

            // Create popup.
            return editor.popups.create('insertCode.popup', template);
        }

        // Show the popup
        function _showPopup() {
            var $highlightBlock = highlightBlock;

            var $popup = editor.popups.get('insertCode.popup');

            if (!$popup) $popup = _initPopup();

            editor.popups.setContainer('insertCode.popup', editor.$sc);

            var offset = $highlightBlock.offset();
            offset.width = $highlightBlock.outerWidth();
            offset.height = $highlightBlock.outerHeight();

            editor.popups.show('insertCode.popup', offset.left + offset.width / 2, offset.top + offset.height, offset.height);
        }

        // Hide the custom popup.
        function _hidePopup() {
            editor.popups.hide('insertCode.popup');
        }

        function _removeCode() {
            _hidePopup();
            highlightBlock.next('p').remove();
            highlightBlock.remove();
            highlightBlock = null;
        }

        // $('.selector').froalaEditor('insertCode.methodName');
        return {
            _init: _init,
            _removeCode: _removeCode,
            _highlightBlock: _highlightBlock,
            show: show,
            hide: hide
        }
    };

    $.FroalaEditor.DefineIcon('insertCode', {NAME: 'file-code-o'});
    $.FroalaEditor.RegisterCommand('insertCode', {
        title: 'Insert Code',
        icon: 'insertCode',
        undo: false,
        focus: false,
        showOnMobile: false,
        modal: false,
        refreshAfterCallback: false,
        callback: function () {
            this.insertCode.show();
        },
        plugin: 'insertCode'
    });

    $.FroalaEditor.DefineIcon('codeRemove', {NAME: 'trash'});
    $.FroalaEditor.RegisterCommand('codeRemove', {
        title: 'Remove Code',
        undo: false,
        focus: false,
        callback: function () {
            this.insertCode._removeCode();
        }
    });

    $.FroalaEditor.DefineIcon('codeEdit', {NAME: 'edit'});
    $.FroalaEditor.RegisterCommand('codeEdit', {
        title: 'Edit Code',
        undo: false,
        focus: false,
        callback: function () {
            var text = [];
            this.insertCode._highlightBlock().find('.ace_line').each(function () {
                var tempText = this.innerText;
                if (!/\n$/.test(tempText))
                    tempText += '\n';
                text.push(tempText);
            });
            this.insertCode.show(text.join(''));
        }
    });
})(jQuery);