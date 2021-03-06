/*******************************************************************************
 * Copyright (c) 2014, 2014 Bruno Medeiros and other Contributors.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Bruno Medeiros - initial API and implementation
 *******************************************************************************/
package com.googlecode.goclipse.ui.editor.actions;


import static melnorme.lang.ide.ui.editor.EditorUtils.getEditorDocument;
import static melnorme.utilbox.core.CoreUtil.areEqual;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.texteditor.ITextEditor;

import com.googlecode.goclipse.core.GoProjectEnvironment;
import com.googlecode.goclipse.core.operations.GoToolManager;
import com.googlecode.goclipse.tooling.env.GoEnvironment;
import com.googlecode.goclipse.ui.editor.GoEditor;

import melnorme.lang.ide.core.LangCore;
import melnorme.lang.ide.ui.editor.actions.AbstractEditorOperation2;
import melnorme.utilbox.concurrency.OperationCancellation;
import melnorme.utilbox.core.CommonException;
import melnorme.utilbox.process.ExternalProcessHelper.ExternalProcessResult;

public abstract class AbstractEditorGoToolOperation extends AbstractEditorOperation2<String> {
	
	protected GoEditor goEditor;
	protected String editorText;
	protected String toolPath;
	protected ProcessBuilder pb;
	
	public AbstractEditorGoToolOperation(String operationName, ITextEditor editor) {
		super(operationName, editor);
	}
	
	@Override
	protected void prepareOperation() throws CoreException {
		if(!(editor instanceof GoEditor)) {
			throw LangCore.createCoreException("Editor is not a GoEditor.", null);
		}
		goEditor = (GoEditor) editor;
		editorText = getEditorDocument(editor).get();
		
		GoEnvironment goEnv = GoProjectEnvironment.getGoEnvironment(null);
		
		try {
			prepareProcessBuilder(goEnv);
		} catch (CommonException ce) {
			throw LangCore.createCoreException(ce);
		}
	}
	
	protected abstract void prepareProcessBuilder(GoEnvironment goEnv) throws CoreException, CommonException;
	
	@Override
	protected String doBackgroundValueComputation(IProgressMonitor monitor)
			throws CoreException, CommonException, OperationCancellation {
		ExternalProcessResult processResult = 
				GoToolManager.getDefault().newRunToolTask(pb, null, monitor).runProcess(editorText, true);
		
		return processResult.getStdOutBytes().toString();
	}
	
	@Override
	protected void handleComputationResult() throws CoreException {
		if(!areEqual(result, editorText)) {
			replaceText(goEditor.getSourceViewer_(), result);
		}
	}
	
	
	public static void replaceText(ISourceViewer sourceViewer, String newText) {
		ISelection sel = sourceViewer.getSelectionProvider().getSelection();
		int topIndex = sourceViewer.getTopIndex();
		
		sourceViewer.getDocument().set(newText);
		
		if (sel != null) {
			sourceViewer.getSelectionProvider().setSelection(sel);
		}
		
		if (topIndex != -1) {
			sourceViewer.setTopIndex(topIndex);
		}
	}
	
}