/*!
 * froala_editor v2.9.1 (https://www.froala.com/wysiwyg-editor)
 * License https://froala.com/wysiwyg-editor/terms/
 * Copyright 2014-2018 Froala Labs
 */
(function ($) {
    var replaceChar = function (str) {
        return str.replace(/</gm, '&lt;').replace(/>/gm, '&gt;');
    };
    /**
     * ace editor support languages.
     * @type {string[]} language name list.they came from mode-xxx.js, 'xxx' is the language name
     */
    var defaultLanguageList = [
        'abap', 'abc', 'actionscript', 'ada', 'apache_conf', 'apex', 'applescript', 'asciidoc', 'asl', 'assembly_x86',
        'autohotkey', 'batchfile', 'bro', 'c9search', 'c_cpp', 'cirru', 'clojure', 'cobol', 'coffee', 'coldfusion',
        'csharp', 'csound_document', 'csound_orchestra', 'csound_score', 'csp', 'css', 'curly', 'd', 'dart', 'diff',
        'django', 'dockerfile', 'dot', 'drools', 'edifact', 'eiffel', 'ejs', 'elixir', 'elm', 'erlang', 'forth', 'fortran',
        'fsharp', 'fsl', 'ftl', 'gcode', 'gherkin', 'gitignore', 'glsl', 'gobstones', 'golang', 'graphqlschema', 'groovy',
        'haml', 'handlebars', 'haskell', 'haskell_cabal', 'haxe', 'hjson', 'html', 'html_elixir', 'html_ruby', 'ini', 'io',
        'jack', 'jade', 'java', 'javascript', 'json', 'jsoniq', 'jsp', 'jssm', 'jsx', 'julia', 'kotlin', 'latex', 'less',
        'liquid', 'lisp', 'livescript', 'logiql', 'logtalk', 'lsl', 'lua', 'luapage', 'lucene', 'makefile', 'markdown',
        'mask', 'matlab', 'maze', 'mel', 'mixal', 'mushcode', 'mysql', 'nix', 'nsis', 'objectivec', 'ocaml', 'pascal',
        'perl', 'perl6', 'pgsql', 'php', 'php_laravel_blade', 'pig', 'plain_text', 'powershell', 'praat', 'prolog', 'properties',
        'protobuf', 'puppet', 'python', 'r', 'razor', 'rdoc', 'red', 'redshift', 'rhtml', 'rst', 'ruby', 'rust', 'sass',
        'scad', 'scala', 'scheme', 'scss', 'sh', 'sjs', 'slim', 'smarty', 'snippets', 'soy_template', 'space', 'sparql',
        'sql', 'sqlserver', 'stylus', 'svg', 'swift', 'tcl', 'terraform', 'tex', 'text', 'textile', 'toml', 'tsx', 'turtle',
        'twig', 'typescript', 'vala', 'vbscript', 'velocity', 'verilog', 'vhdl', 'visualforce', 'wollok', 'xml', 'xquery',
        'yaml'
    ];

    /**
     * language alias.for example:if you want to show c_cpp as C/C++ , you can add a property like c_cpp:'C/C++' below
     */
    var defaultLanguageAlias = {
        c_cpp: 'c/cpp',
        csound_document: 'csound document',
        csound_orchestra: 'csound orchestra',
        csound_score: 'csound score',
        ftl: 'ftl (freemarker)',
        haskell_cabal: 'haskell cabal',
        html_elixir: 'html elixir',
        html_ruby: 'html ruby',
        php_laravel_blade: 'php laravel blade',
        plain_text: 'plain text',
        soy_template: 'soy template'

    };

    /**
     * ace editor's default language
     * @type {string} language name in default language list
     */
    var defaultLanguage = 'html';

    /**
     * ace editor support themes
     * @type {string[]} theme name list.they came from theme-xxx.js, 'xxx' is the theme name
     */
    var defaultThemeList = [
        'ambiance', 'chaos', 'chrome', 'clouds', 'clouds_midnight', 'cobalt', 'crimson_editor', 'dawn', 'dracula',
        'dreamweaver', 'eclipse', 'github', 'gob', 'gruvbox', 'idle_fingers', 'iplastic', 'katzenmilch', 'kr_theme',
        'kuroir', 'merbivore', 'merbivore_soft', 'mono_industrial', 'monokai', 'pastel_on_dark', 'solarized_dark',
        'solarized_light', 'sqlserver', 'terminal', 'textmate', 'tomorrow', 'tomorrow_night', 'tomorrow_night_blue',
        'tomorrow_night_bright', 'tomorrow_night_eighties', 'twilight', 'vibrant_ink', 'xcode'
    ];

    /**
     * ace editor's default theme
     * @type {string} theme name in default theme list
     */
    var defaultTheme = 'tomorrow';

    /**
     * merge default options to FroalaEditor's DEFAULTS options
     */
    $.FroalaEditor.DEFAULTS = $.extend($.FroalaEditor.DEFAULTS, {
        insertCodeOption: {
            defaultLanguage: defaultLanguage,
            languages: defaultLanguageList,
            defaultTheme: defaultTheme,
            themes: defaultThemeList
        }
    });

    /**
     * add a new popup to FroalaEditor called 'insertCode.popup'.it only show some buttons.
     * if we need some content , we should change [_BUTTONS_] to [_BUTTONS_][_CUSTOM_LAYER_]
     */
    $.extend($.FroalaEditor.POPUP_TEMPLATES, {
        "insertCode.popup": '[_BUTTONS_]'
    });

    /**
     * get rand number
     * @param num rand number length
     * @returns {number} rand number
     */
    function getRandom(num) {
        return Math.floor((Math.random() + Math.floor(Math.random() * 9 + 1)) * Math.pow(10, num - 1));
    }

    /**
     * define plugin
     * @param editor froala editor instance
     * @returns {{_removeCode: _removeCode, hide: hide, _init: _init, _highlightBlock: (function(): *), show: show, _getOption: (function(*, *=): *)}}
     */
    $.FroalaEditor.PLUGINS.insertCode = function (editor) {
        var insertButtonName = editor.language.translate("Insert");
        var updateButtonName = editor.language.translate("Update");
        var cancelButtonName = editor.language.translate("Cancel");

        //插件名字加上随机数,用于产生多个插件实例
        var pluginName = 'insert_code_' + getRandom(10);
        var insertCodeWindow, insertCodeHead, insertCodeBody, codeEditor, highlightBlock;

        /**
         * get current code editor instance's option
         * @param optionName option name
         * @param defaultOption default option if user not set it
         * @returns {*} the final option
         * @private this is a private method.do not use it in public
         */
        function _getOption(optionName, defaultOption) {
            return editor.opts.insertCodeOption[optionName] || defaultOption;
        }

        /**
         * get current highlight code block
         * @returns {*} current highlight code block.it's a jquery instance
         * @private this is a private method.do not use it in public
         */
        function _highlightBlock() {
            return highlightBlock;
        }

        /**
         * show insert code window
         * @param content default code content.it can be null
         * @param language default code editor language.it can be null
         * @param theme default code editor theme.it can be null
         */
        function show(content, language, theme) {
            // if(content)
            //     content = replaceChar(content);
            if (!insertCodeWindow) {
                var tempLanguageList = _getOption('languages', defaultLanguageList);
                var tempDefaultLanguage = language || _getOption('defaultLanguage', defaultLanguage);
                var titleHtml = "<h4>" + editor.language.translate("Insert Code") + "</h4>",
                    contentHtml = "<pre class='insert-code-content'></pre>",
                    modal = editor.modals.create(pluginName, titleHtml, contentHtml);
                insertCodeWindow = modal.$modal;
                insertCodeHead = modal.$head;
                insertCodeBody = modal.$body;
                insertCodeHead.append(function () {
                    var select = [];
                    select.push('<span class="insert-code-header-selector-label">' + editor.language.translate('Code Language') + '：</span>');
                    select.push('<select class="insert-code-choose-code-language">');
                    for (var i = 0; i < tempLanguageList.length; i++) {
                        var tempLanguage = tempLanguageList[i];
                        var languageAlias = defaultLanguageAlias.hasOwnProperty(tempLanguage) ? defaultLanguageAlias[tempLanguage] : tempLanguage;
                        if (tempDefaultLanguage == null && i === 0) {
                            select.push('<option value="' + tempLanguage + '" selected>' + languageAlias + '</option>');
                        } else {
                            if (tempLanguage === tempDefaultLanguage) {
                                select.push('<option value="' + tempLanguage + '" selected>' + languageAlias + '</option>');
                            } else {
                                select.push('<option value="' + tempLanguage + '">' + languageAlias + '</option>');
                            }
                        }
                    }
                    select.push('</select>');
                    return select.join('');
                });
                var tempThemeList = _getOption('themes', defaultThemeList);
                var tempDefaultTheme = theme || _getOption('defaultTheme', defaultTheme);
                insertCodeHead.append(function () {
                    var select = [];
                    select.push('<span class="insert-code-header-selector-label">' + editor.language.translate('Code Editor Theme') + '：</span>');
                    select.push('<select class="insert-code-choose-code-theme">');
                    for (var i = 0; i < tempThemeList.length; i++) {
                        var tempTheme = tempThemeList[i];
                        if (tempDefaultTheme == null && i === 0) {
                            select.push('<option value="' + tempTheme + '" selected>' + tempTheme.replace(/_/g, ' ') + '</option>');
                        } else {
                            if (tempTheme === tempDefaultTheme || tempTheme === theme) {
                                select.push('<option value="' + tempTheme + '" selected>' + tempTheme.replace(/_/g, ' ') + '</option>');
                            } else {
                                select.push('<option value="' + tempTheme + '">' + tempTheme.replace(/_/g, ' ') + '</option>');
                            }
                        }
                    }
                    select.push('</select>');
                    return select.join('');
                });
                insertCodeHead.css({
                    zIndex: 100,
                    textAlign: 'left'
                });
                insertCodeBody.css({
                    textAlign: 'left',
                    overflow: 'auto',
                    paddingBottom: 0
                });
                insertCodeBody.append('<div class="insert-code-content-buttons">' +
                    '<button class="fr-command insert">' + insertButtonName + '</button>' +
                    '<button class="fr-command cancel">' + cancelButtonName + '</button>' +
                    '</div>');
                editor.events.$on($(editor.o_win), "resize", function () {
                    (insertCodeWindow.data("instance") || editor).modals.resize(pluginName)
                });
                editor.events.bindClick(insertCodeBody, ".insert", function (e) {
                    editor.insertCode.hide();
                    var tempId, value = replaceChar(codeEditor.getValue());
                    if (value.trim().length === 0)
                        return;
                    editor.undo.saveStep();
                    if (highlightBlock) {
                        tempId = highlightBlock.children('pre').attr('id');
                        // noinspection HtmlUnknownAttribute
                        var tempHighlightBlock = $('<div class="ace_code_highlight_container" ace-mode="' + insertCodeHead.find('.insert-code-choose-code-language').val() +
                            '" contenteditable="false" ><pre id="' + tempId + '" class="ace_code_highlight" ' +
                            'ace-mode="ace/mode/' + insertCodeHead.find('.insert-code-choose-code-language').val() + '" ' +
                            'ace-theme="ace/theme/' + insertCodeHead.find('.insert-code-choose-code-theme').val() + '" ' +
                            'ace-gutter="true">' + value + '</pre></div>');
                        highlightBlock.replaceWith(tempHighlightBlock);
                        highlightBlock = tempHighlightBlock;
                        // _staticHighlight($('#' + tempId));
                    } else {
                        tempId = 'ace_code_highlight_' + getRandom(10);
                        // noinspection HtmlUnknownAttribute
                        editor.html.insert('<div class="ace_code_highlight_container" ace-mode="' + insertCodeHead.find('.insert-code-choose-code-language').val() +
                            '" contenteditable="false" ><pre id="' + tempId + '" class="ace_code_highlight" ' +
                            'ace-mode="ace/mode/' + insertCodeHead.find('.insert-code-choose-code-language').val() + '" ' +
                            'ace-theme="ace/theme/' + insertCodeHead.find('.insert-code-choose-code-theme').val() + '" ' +
                            'ace-gutter="true">' + value + '</pre></div><p><br></p>', true);
                        highlightBlock = null;
                        // _staticHighlight($('#' + tempId));
                    }
                    editor.undo.saveStep();
                });
                editor.events.bindClick(insertCodeBody, ".cancel", function () {
                    (insertCodeWindow.data("instance") || editor).insertCode.hide();
                }, false);
                editor.events.$on(insertCodeHead, 'change', function (e) {
                    var target = e.target, mode, theme;
                    if (target.className.indexOf('insert-code-choose-code-language') !== -1) {
                        mode = e.target.value;
                        theme = insertCodeHead.find('.insert-code-choose-code-theme').val();
                    } else if (target.className.indexOf('insert-code-choose-code-theme') !== -1) {
                        theme = e.target.value;
                        mode = insertCodeHead.find('.insert-code-choose-code-language').val();
                    }
                    codeEditor.session.setMode("ace/mode/" + mode);
                    codeEditor.setTheme("ace/theme/" + theme);
                });
            }
            if (!codeEditor) {
                codeEditor = _initCodeEditor(editor.modals.get(pluginName).$body.find('.insert-code-content').get(0), tempDefaultLanguage, defaultTheme);
            }
            if (!content) {
                codeEditor.setValue('');
            } else {
                //-1表示到第一行,1表示到最后一行
                codeEditor.setValue(content, 1);
            }
            var tempBody = editor.modals.get(pluginName).$body;
            if (content) {
                tempBody.find('.insert').text(updateButtonName);
            } else {
                tempBody.find('.insert').text(insertButtonName);
            }
            if (language) {
                codeEditor.session.setMode("ace/mode/" + language);
                editor.modals.get(pluginName).$head.find('.insert-code-choose-code-language').val(language);
            } else {
                codeEditor.session.setMode("ace/mode/" + defaultLanguage);
                editor.modals.get(pluginName).$head.find('.insert-code-choose-code-language').val(_getOption('defaultLanguage', defaultLanguage));
            }

            if (theme) {
                codeEditor.setTheme("ace/theme/" + theme);
                editor.modals.get(pluginName).$head.find('.insert-code-choose-code-theme').val(theme);
            } else {
                codeEditor.setTheme("ace/theme/" + _getOption('defaultTheme', defaultTheme));
                editor.modals.get(pluginName).$head.find('.insert-code-choose-code-theme').val(_getOption('defaultTheme', defaultTheme));
            }
            editor.modals.show(pluginName);
            editor.modals.resize(pluginName);
        }

        /**
         * hide insert code window
         */
        function hide() {
            editor.modals.hide(pluginName)
        }

        /**
         * define show tool popup event when click code highlight block
         * @param event click event
         * @private this is a private method.do not use it in public
         */
        function _showToolPopup(event) {
            if (editor.core.hasFocus() && event) {
                var $target = $(event.target);
                if ($target.closest('.ace_code_highlight').length === 1 && $target.closest('.fr-view').length === 1) {
                    highlightBlock = $target.closest('.ace_code_highlight').parent();
                    _showPopup();
                } else {
                    highlightBlock = null;
                }
            }
        }

        /**
         * init ace code editor
         * @param codeEditorElement editor element.it's a dom element,not a jquery instance
         * @param defaultLanguage editor code language
         * @param defaultTheme editor theme
         * @returns {*} ace code editor instance
         * @private this is a private method.do not use it in public
         */
        function _initCodeEditor(codeEditorElement, defaultLanguage, defaultTheme) {
            ace.require("ace/ext/language_tools");
            var tempCodeEditor = ace.edit(codeEditorElement);
            tempCodeEditor.session.setMode("ace/mode/" + defaultLanguage);
            tempCodeEditor.setTheme("ace/theme/" + defaultTheme);
            tempCodeEditor.setOptions({
                autoScrollEditorIntoView: true,
                enableBasicAutocompletion: true,
                enableSnippets: true,
                enableLiveAutocompletion: true
            });
            return tempCodeEditor;
        }

        /**
         * define static highlight method.it's used for highlighting insert code
         * @param $elements the elements which need to highlight.it's a jquery instance
         * @private this is a private method.do not use it in public
         */
        function _staticHighlight($elements) {
            var highlight = ace.require("ace/ext/static_highlight");
            $elements.each(function () {
                if ($(this).find('.ace_static_highlight').length === 0) {
                    highlight(this, {
                        mode: this.getAttribute("ace-mode"),
                        theme: this.getAttribute("ace-theme"),
                        startLineNumber: 1,
                        showGutter: this.getAttribute("ace-gutter"),
                        trim: false
                    }, function (highlighted) {

                    });
                }
            });
        }

        /**
         * default init method.
         * @private this is a private method.do not use it in public
         */
        function _init() {
            editor.events.on("window.mouseup", _showToolPopup);
            editor.events.$on(editor.$wp, "scroll.insertCode-popup", function (e) {
                editor.popups.isVisible("insertCode.popup") && _showPopup();
            });
            editor.events.on('snapshot.before',function () {
                editor.insertCode.planCode.call(editor);
            })
        }

        /**
         * init code block's tool popup when click it
         * @returns {insertCode.popup} initialized popup
         * @private this is a private method.do not use it in public
         */
        function _initPopup() {
            var popup_buttons = '';
            popup_buttons += '<div class="fr-buttons">';
            popup_buttons += editor.button.buildList(['codeRemove', 'codeEdit', 'codeTheme', 'insertLineBefore', 'deleteLineBefore']);
            popup_buttons += '</div>';
            var template = {
                buttons: popup_buttons
            };
            return editor.popups.create('insertCode.popup', template);
        }

        /**
         * show code block's tool popup when click it
         * @private this is a private method.do not use it in public
         */
        function _showPopup() {
            var $highlightBlock = highlightBlock.children('pre');
            var $popup = editor.popups.get('insertCode.popup');
            if (!$popup) {
                $popup = _initPopup();
                editor.popups.onHide("insertCode.popup", function () {
                    planCode(this.insertCode._highlightBlock());
                });
            }
            editor.popups.setContainer('insertCode.popup', editor.$sc);
            var offset = $highlightBlock.offset();
            offset.width = $highlightBlock.outerWidth();
            offset.height = $highlightBlock.outerHeight();
            editor.popups.show('insertCode.popup', offset.left + offset.width / 2, offset.top + offset.height, offset.height);
            _staticHighlight($highlightBlock);
        }

        function planCode($highlightBlock) {
            if (!$highlightBlock)
                $highlightBlock = this.$el.find('.ace_code_highlight_container');
            $highlightBlock.each(function () {
                var text = [];
                var highlightBlock = $(this);
                highlightBlock.children('pre').attr('class', 'ace_code_highlight');
                var $aceLines = highlightBlock.find('.ace_line');
                if ($aceLines.length > 0) {
                    $aceLines.each(function (index) {
                        var tempText = this.innerText;
                        if (index < $aceLines.length - 1) {
                            if (/\n+$/.test(tempText)) {
                                tempText = tempText.replace(/\n+$/, '\n');
                            } else {
                                tempText += '\n';
                            }
                        } else {
                            tempText = tempText.replace(/\n+$/, '');
                        }
                        text.push(tempText);
                    });
                    if (text.length === 0)
                        text.push(highlightBlock.text());
                    highlightBlock.children('pre').text(text.join(''));
                }
            });
        }

        /**
         * hide code block's tool popup
         * @private this is a private method.do not use it in public
         */
        function _hidePopup() {
            editor.popups.hide('insertCode.popup');
        }

        /**
         * remove current active highlight code block;
         * @private this is a private method.do not use it in public
         */
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
            _getOption: _getOption,
            _hidePopup: _hidePopup,
            show: show,
            hide: hide,
            planCode: planCode
        }
    };

    /**
     * define a icon called insertCode.we can add it to froala editor's toolbar
     */
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

    /**
     * define a icon called codeRemove.do not add it to froala editor's toolbar
     */
    $.FroalaEditor.DefineIcon('codeRemove', {NAME: 'trash'});
    $.FroalaEditor.RegisterCommand('codeRemove', {
        title: 'Remove Code',
        undo: true,
        focus: false,
        callback: function () {
            this.insertCode._removeCode();
        }
    });

    /**
     * define a icon called codeTheme.do not add it to froala editor's toolbar
     */
    $.FroalaEditor.DefineIcon('codeTheme', {NAME: 'magic'});
    $.FroalaEditor.RegisterCommand('codeTheme', {
        title: 'Code Theme',
        type: 'dropdown',
        undo: false,
        focus: false,
        refreshAfterCallback: true,
        html: function () {
            var tempThemeList = this.insertCode._getOption('themes', defaultTheme);
            var options = ['<ul class="fr-dropdown-list" role="presentation">'];
            for (var i = 0; i < tempThemeList.length; i++) {
                var t = tempThemeList[i];
                options.push('<li role="presentation"><a class="fr-command ' + t + '" tabIndex="-1" role="option" data-cmd="codeTheme" data-param1="' + t + '" title="' + t.replace(/_/g, '') + '">' + t.replace(/_/g, '') + "</a></li>");
            }
            options.push('</ul>');
            return options.join('');
        },
        callback: function (cmd, val) {
            var highlightBlock = this.insertCode._highlightBlock().children('pre');
            highlightBlock.attr('ace-theme', 'ace/theme/' + val);
            var highlight = ace.require("ace/ext/static_highlight");
            highlight.loadTheme(highlightBlock.attr('ace-theme'), function (themeModule) {
                highlightBlock.attr('class', 'ace_code_highlight ' + themeModule.cssClass);
                highlightBlock.children().attr('class', themeModule.cssClass);
            });
        },
        refreshOnShow: function ($btn, $dropdown) {
            var theme = this.insertCode._highlightBlock().children('pre').attr('ace-theme').replace(/ace\/theme\//, '');
            $dropdown.find(".fr-command.fr-active").removeClass('fr-active').attr('aria-selected', false);
            $dropdown.find(".fr-command[data-param1='" + theme + "']").addClass('fr-active').attr('aria-selected', true);
        }
    });

    /**
     * define a icon called codeEdit.do not add it to froala editor's toolbar
     */
    $.FroalaEditor.DefineIcon('codeEdit', {NAME: 'edit'});
    $.FroalaEditor.RegisterCommand('codeEdit', {
        title: 'Edit Code',
        undo: false,
        focus: false,
        callback: function () {
            var text = [];
            var highlightBlock = this.insertCode._highlightBlock().children('pre');
            highlightBlock.find('.ace_line').each(function () {
                var tempText = this.innerText;
                if (!/\n$/.test(tempText))
                    tempText += '\n';
                text.push(tempText);
            });
            this.insertCode.show(text.join(''), highlightBlock.attr('ace-mode').replace(/ace\/mode\//, ''), highlightBlock.attr('ace-theme').replace(/ace\/theme\//, ''));
        }
    });

    /**
     * define a icon called newLineBefore.do not add it to froala editor's toolbar
     */
    $.FroalaEditor.DefineIcon('insertLineBefore', {NAME: 'plus'});
    $.FroalaEditor.RegisterCommand('insertLineBefore', {
        title: 'Prepend Wrap Line',
        undo: true,
        focus: false,
        callback: function () {
            var highlightBlock = this.insertCode._highlightBlock();
            highlightBlock.before('<p><br></p>');
            this.insertCode._hidePopup();
        }
    });

    /**
     * define a icon called newLineAfter.do not add it to froala editor's toolbar
     */
    $.FroalaEditor.DefineIcon('deleteLineBefore', {NAME: 'minus'});
    $.FroalaEditor.RegisterCommand('deleteLineBefore', {
        title: 'Delete Wrap Line',
        undo: true,
        focus: false,
        callback: function () {
            var highlightBlock = this.insertCode._highlightBlock();
            highlightBlock.prev('p').remove();
            this.insertCode._hidePopup();
        }
    });
})(jQuery);