/*
Copyright (c) 2003-2009, CKSource - Frederico Knabben. All rights reserved.
For licensing, see LICENSE.html or http://ckeditor.com/license
*/

CKEDITOR.editorConfig = function( config )
{
		config.toolbar = 'MyToolbar'; 
		config.toolbar_MyToolbar = 
		[
			['Source','-','Save','NewPage','Preview'], 
			['Cut','Copy','Paste','PasteText','PasteFromWord','-','Print'], 
			['Undo','Redo','-','Find','Replace','-','SelectAll','RemoveFormat'], 
			['Form', 'Checkbox', 'Radio', 'TextField', 'Textarea', 'Select', 'Button', 'ImageButton', 'HiddenField'], '/', 
			['Bold','Italic','Underline','Strike','-','Subscript','Superscript'], 
			['NumberedList','BulletedList','-','Outdent','Indent','Blockquote'], 
			['JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock'], 
			['Link','Unlink','Anchor'], 
			['Image','Flash','Table','HorizontalRule','Smiley','SpecialChar','PageBreak'], '/', 
			['Styles','Format','Font','FontSize'], ['TextColor','BGColor'], 
			['Maximize', 'ShowBlocks'] 
		];
		config.skin = 'office2003';
		config.enterMode = CKEDITOR.ENTER_BR;

};
