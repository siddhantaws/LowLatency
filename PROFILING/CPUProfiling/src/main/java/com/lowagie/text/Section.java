/*
 * $Id: Section.java 2945 2007-09-27 17:34:24Z psoares33 $
 * $Name$
 *
 * Copyright 1999, 2000, 2001, 2002 by Bruno Lowagie.
 *
 * The contents of this file are subject to the Mozilla Public License Version 1.1
 * (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the License.
 *
 * The Original Code is 'iText, a free JAVA-PDF library'.
 *
 * The Initial Developer of the Original Code is Bruno Lowagie. Portions created by
 * the Initial Developer are Copyright (C) 1999, 2000, 2001, 2002 by Bruno Lowagie.
 * All Rights Reserved.
 * Co-Developer of the code is Paulo Soares. Portions created by the Co-Developer
 * are Copyright (C) 2000, 2001, 2002 by Paulo Soares. All Rights Reserved.
 *
 * Contributor(s): all the names of the contributors are added in the source code
 * where applicable.
 *
 * Alternatively, the contents of this file may be used under the terms of the
 * LGPL license (the "GNU LIBRARY GENERAL PUBLIC LICENSE"), in which case the
 * provisions of LGPL are applicable instead of those above.  If you wish to
 * allow use of your version of this file only under the terms of the LGPL
 * License and not to allow others to use your version of this file under
 * the MPL, indicate your decision by deleting the provisions above and
 * replace them with the notice and other provisions required by the LGPL.
 * If you do not delete the provisions above, a recipient may use your version
 * of this file under either the MPL or the GNU LIBRARY GENERAL PUBLIC LICENSE.
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the MPL as stated above or under the terms of the GNU
 * Library General Public License as published by the Free Software Foundation;
 * either version 2 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library general Public License for more
 * details.
 *
 * If you didn't download this code from the following link, you should check if
 * you aren't using an obsolete version:
 * http://www.lowagie.com/iText/
 */

package com.lowagie.text;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A <CODE>Section</CODE> is a part of a <CODE>Document</CODE> containing
 * other <CODE>Section</CODE>s, <CODE>Paragraph</CODE>s, <CODE>List</CODE>
 * and/or <CODE>Table</CODE>s.
 * <P>
 * Remark: you can not construct a <CODE>Section</CODE> yourself.
 * You will have to ask an instance of <CODE>Section</CODE> to the
 * <CODE>Chapter</CODE> or <CODE>Section</CODE> to which you want to
 * add the new <CODE>Section</CODE>.
 * <P>
 * Example:
 * <BLOCKQUOTE><PRE>
 * Paragraph title2 = new Paragraph("This is Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLDITALIC, new Color(0, 0, 255)));
 * Chapter chapter2 = new Chapter(title2, 2);
 * Paragraph someText = new Paragraph("This is some text");
 * chapter2.add(someText);
 * Paragraph title21 = new Paragraph("This is Section 1 in Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section1 = chapter2.addSection(title21);</STRONG>
 * Paragraph someSectionText = new Paragraph("This is some silly paragraph in a chapter and/or section. It contains some text to test the functionality of Chapters and Section.");
 * <STRONG>section1.add(someSectionText);</STRONG>
 * Paragraph title211 = new Paragraph("This is SubSection 1 in Section 1 in Chapter 2", FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD, new Color(255, 0, 0)));
 * <STRONG>Section section11 = section1.addSection(40, title211, 2);</STRONG>
 * <STRONG>section11.add(someSectionText);</STRONG>
 * </PRE></BLOCKQUOTE>
 */

public class Section extends ArrayList implements TextElementArray {
    
    // constant
	private static final long serialVersionUID = 3324172577544748043L;

	// member variables
	
	/** The title of this section. */
    protected Paragraph title;
    
    /** The bookmark title if different from the content title */
    protected String bookmarkTitle;
    
    /** The number of sectionnumbers that has to be shown before the section title. */
    protected int numberDepth;
    
    /** The indentation of this section on the left side. */
    protected float indentationLeft;
    
    /** The indentation of this section on the right side. */
    protected float indentationRight;
    
    /** The additional indentation of the content of this section. */
    protected float indentation;
    
    /** false if the bookmark children are not visible */
    protected boolean bookmarkOpen = true;
    
    /** true if the section has to trigger a new page */
    protected boolean triggerNewPage = false;
    
    /** This is the number of subsections. */
    protected int subsections = 0;
    
    /** This is the complete list of sectionnumbers of this section and the parents of this section. */
    protected ArrayList numbers = null;
    
    // constructors
    
    /**
     * Constructs a new <CODE>Section</CODE>.
     */    
    protected Section() {
        title = new Paragraph();
        numberDepth = 1;
    }
    
    /**
     * Constructs a new <CODE>Section</CODE>.
     *
     * @param	title			a <CODE>Paragraph</CODE>
     * @param	numberDepth		the numberDepth
     */
    protected Section(Paragraph title, int numberDepth) {
        this.numberDepth = numberDepth;
        this.title = title;
    }
    
    // implementation of the Element-methods
    
    /**
     * Processes the element by adding it (or the different parts) to an
     * <CODE>ElementListener</CODE>.
     *
     * @param	listener		the <CODE>ElementListener</CODE>
     * @return	<CODE>true</CODE> if the element was processed successfully
     */
    public boolean process(ElementListener listener) {
        try {
        	Element element;
            for (Iterator i = iterator(); i.hasNext(); ) {
            	element = (Element)i.next();
                listener.add(element);
            }
            return true;
        }
        catch(DocumentException de) {
            return false;
        }
    }
    
    /**
     * Gets the type of the text element.
     *
     * @return	a type
     */    
    public int type() {
        return Element.SECTION;
    }
    
    /**
     * Checks if this object is a <CODE>Chapter</CODE>.
     *
     * @return	<CODE>true</CODE> if it is a <CODE>Chapter</CODE>,
     *			<CODE>false</CODE> if it is a <CODE>Section</CODE>.
     */
    public boolean isChapter() {
        return type() == Element.CHAPTER;
    }
    
    /**
     * Checks if this object is a <CODE>Section</CODE>.
     *
     * @return	<CODE>true</CODE> if it is a <CODE>Section</CODE>,
     *			<CODE>false</CODE> if it is a <CODE>Chapter</CODE>.
     */
    public boolean isSection() {
        return type() == Element.SECTION;
    }
    
    /**
     * Gets all the chunks in this element.
     *
     * @return	an <CODE>ArrayList</CODE>
     */
    public ArrayList getChunks() {
        ArrayList tmp = new ArrayList();
        for (Iterator i = iterator(); i.hasNext(); ) {
            tmp.addAll(((Element) i.next()).getChunks());
        }
        return tmp;
    }
    
    // overriding some of the ArrayList-methods
    
    /**
     * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
     * to this <CODE>Section</CODE>.
     *
     * @param	index	index at which the specified element is to be inserted
     * @param	o   	an object of type <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>=
     * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE> or <CODE>Table</CODE>
     */
    public void add(int index, Object o) {
        try {
            Element element = (Element) o;
            if (element.type() == Element.PARAGRAPH ||
            element.type() == Element.LIST ||
            element.type() == Element.CHUNK ||
            element.type() == Element.PHRASE ||
            element.type() == Element.ANCHOR ||
            element.type() == Element.ANNOTATION ||
            element.type() == Element.TABLE ||
            element.type() == Element.PTABLE ||
            element.type() == Element.IMGTEMPLATE ||
            element.type() == Element.JPEG ||
            element.type() == Element.JPEG2000 ||
            element.type() == Element.IMGRAW) {
                super.add(index, element);
            }
            else {
                throw new ClassCastException("You can add a " + element.getClass().getName() + " to a Section.");
            }
        }
        catch(ClassCastException cce) {
            throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
        }
    }
    
    /**
     * Adds a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
     * to this <CODE>Section</CODE>.
     *
     * @param	o   	an object of type <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or another <CODE>Section</CODE>
     * @return	a boolean
     * @throws	ClassCastException if the object is not a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE> or <CODE>Section</CODE>
     */
    public boolean add(Object o) {
        try {
            Element element = (Element) o;
            if (element.type() == Element.PARAGRAPH ||
            element.type() == Element.LIST ||
            element.type() == Element.CHUNK ||
            element.type() == Element.PHRASE ||
            element.type() == Element.ANCHOR ||
            element.type() == Element.ANNOTATION ||
            element.type() == Element.TABLE ||
            element.type() == Element.IMGTEMPLATE ||
            element.type() == Element.PTABLE ||
            element.type() == Element.JPEG ||
            element.type() == Element.JPEG2000 ||
            element.type() == Element.IMGRAW) {
                return super.add(o);
            }
            else if (element.type() == Element.SECTION) {
                Section section = (Section) o;
                section.setNumbers(++subsections, numbers);
                return super.add(section);
            }
            else if (o instanceof MarkedSection && ((MarkedObject)o).element.type() == Element.SECTION) {
            	MarkedSection mo = (MarkedSection)o;
            	Section section = (Section)mo.element;
            	section.setNumbers(++subsections, numbers);
            	return super.add(mo);
            }
            else if (element instanceof MarkedObject) {
            	return super.add(o);
            }
            else {
                throw new ClassCastException("You can add a " + element.getClass().getName() + " to a Section.");
            }
        }
        catch(ClassCastException cce) {
            throw new ClassCastException("Insertion of illegal Element: " + cce.getMessage());
        }
    }
    
    /**
     * Adds a collection of <CODE>Element</CODE>s
     * to this <CODE>Section</CODE>.
     *
     * @param	collection	a collection of <CODE>Paragraph</CODE>s, <CODE>List</CODE>s and/or <CODE>Table</CODE>s
     * @return	<CODE>true</CODE> if the action succeeded, <CODE>false</CODE> if not.
     * @throws	ClassCastException if one of the objects isn't a <CODE>Paragraph</CODE>, <CODE>List</CODE>, <CODE>Table</CODE>
     */
    public boolean addAll(Collection collection) {
        for (Iterator iterator = collection.iterator(); iterator.hasNext(); ) {
            this.add(iterator.next());
        }
        return true;
    }
    
    // methods that return a Section
    
    /**
     * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
     *
     * @param	indentation	the indentation of the new section
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(float indentation, Paragraph title, int numberDepth) {
        Section section = new Section(title, numberDepth);
        section.setIndentation(indentation);
        add(section);
        return section;
    }
    
    /**
     * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
     *
     * @param	indentation	the indentation of the new section
     * @param	title		the title of the new section
     * @return  a new Section object
     */
    public Section addSection(float indentation, Paragraph title) {
        return addSection(indentation, title, numberDepth + 1);
    }
    
    /**
     * Creates a <CODE>Section</CODE>, add it to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(Paragraph title, int numberDepth) {
        return addSection(0, title, numberDepth);
    }
    
    /**
     * Adds a marked section. For use in class MarkedSection only!
     */
    public MarkedSection addMarkedSection() {
    	MarkedSection section = new MarkedSection(new Section(null, numberDepth + 1));
    	add(section);
    	return section;
    }
    
    /**
     * Creates a <CODE>Section</CODE>, adds it to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @return  a new Section object
     */
    public Section addSection(Paragraph title) {
        return addSection(0, title, numberDepth + 1);
    }
    
    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	indentation	the indentation of the new section
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(float indentation, String title, int numberDepth) {
        return addSection(indentation, new Paragraph(title), numberDepth);
    }
    
    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @param	numberDepth	the numberDepth of the section
     * @return  a new Section object
     */
    public Section addSection(String title, int numberDepth) {
        return addSection(new Paragraph(title), numberDepth);
    }
    
    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	indentation	the indentation of the new section
     * @param	title		the title of the new section
     * @return  a new Section object
     */
    public Section addSection(float indentation, String title) {
        return addSection(indentation, new Paragraph(title));
    }
    
    /**
     * Adds a <CODE>Section</CODE> to this <CODE>Section</CODE> and returns it.
     *
     * @param	title		the title of the new section
     * @return  a new Section object
     */
    public Section addSection(String title) {
        return addSection(new Paragraph(title));
    }
    
    // public methods
    
    /**
     * Sets the title of this section.
     *
     * @param	title	the new title
     */
    public void setTitle(Paragraph title) {
        this.title = title;
    }

	/**
     * Returns the title, preceeded by a certain number of sectionnumbers.
     *
     * @return	a <CODE>Paragraph</CODE>
     */
    public Paragraph getTitle() {
        if (title == null) {
            return null;
        }
        int depth = Math.min(numbers.size(), numberDepth);
        if (depth < 1) {
            return title;
        }
        StringBuffer buf = new StringBuffer(" ");
        for (int i = 0; i < depth; i++) {
            buf.insert(0, ".");
            buf.insert(0, ((Integer) numbers.get(i)).intValue());
        }
        Paragraph result = new Paragraph(title);
        result.add(0, new Chunk(buf.toString(), title.getFont()));
        return result;
    }
    
    /**
     * Sets the depth of the sectionnumbers that will be shown preceding the title.
     * <P>
     * If the numberdepth is 0, the sections will not be numbered. If the numberdepth
     * is 1, the section will be numbered with their own number. If the numberdepth is
     * higher (for instance x > 1), the numbers of x - 1 parents will be shown.
     *
     * @param	numberDepth		the new numberDepth
     */
    public void setNumberDepth(int numberDepth) {
        this.numberDepth = numberDepth;
    }
    
	/**
     * Returns the numberdepth of this <CODE>Section</CODE>.
     *
     * @return	the numberdepth
     */
    public int getNumberDepth() {
        return numberDepth;
    }
    
    /**
     * Sets the indentation of this <CODE>Section</CODE> on the left side.
     *
     * @param	indentation		the indentation
     */
    public void setIndentationLeft(float indentation) {
        indentationLeft = indentation;
    }

	/**
     * Returns the indentation of this <CODE>Section</CODE> on the left side.
     *
     * @return	the indentation
     */
    public float getIndentationLeft() {
        return indentationLeft;
    }
    
    /**
     * Sets the indentation of this <CODE>Section</CODE> on the right side.
     *
     * @param	indentation		the indentation
     */
    public void setIndentationRight(float indentation) {
        indentationRight = indentation;
    }

	/**
     * Returns the indentation of this <CODE>Section</CODE> on the right side.
     *
     * @return	the indentation
     */
    public float getIndentationRight() {
        return indentationRight;
    }
    
    /**
     * Sets the indentation of the content of this <CODE>Section</CODE>.
     *
     * @param	indentation		the indentation
     */
    public void setIndentation(float indentation) {
        this.indentation = indentation;
    }

	/**
     * Returns the indentation of the content of this <CODE>Section</CODE>.
     *
     * @return	the indentation
     */
    public float getIndentation() {
        return indentation;
    }
    
    /** Setter for property bookmarkOpen.
     * @param bookmarkOpen false if the bookmark children are not
     * visible.
     */
    public void setBookmarkOpen(boolean bookmarkOpen) {
        this.bookmarkOpen = bookmarkOpen;
    }
    
    /**
     * Getter for property bookmarkOpen.
     * @return Value of property bookmarkOpen.
     */
    public boolean isBookmarkOpen() {
        return bookmarkOpen;
    }
    
    /**
     * Setter for property triggerNewPage.
     * @param triggerNewPage true if a new page has to be triggered.
     */
	public void setTriggerNewPage(boolean triggerNewPage) {
		this.triggerNewPage = triggerNewPage;
	}

    /**
     * Getter for property bookmarkOpen.
     * @return Value of property triggerNewPage.
     */
    public boolean isTriggerNewPage() {
		return triggerNewPage;
	}
    
    /**
     * Sets the bookmark title. The bookmark title is the same as the section title but
     * can be changed with this method.
     * @param bookmarkTitle the bookmark title
     */    
    public void setBookmarkTitle(String bookmarkTitle) {
        this.bookmarkTitle = bookmarkTitle;
    }

	/**
     * Gets the bookmark title.
     * @return the bookmark title
     */    
    public Paragraph getBookmarkTitle() {
        if (bookmarkTitle == null)
            return getTitle();
        else
            return new Paragraph(bookmarkTitle);
    }
    
    /**
     * Changes the Chapter number.
     */
    public void setChapterNumber(int number) {
    	numbers.set(numbers.size() - 1, new Integer(number));
    	Object s;
    	for (Iterator i = iterator(); i.hasNext(); ) {
    		s = i.next();
    		if (s instanceof Section) {
    			((Section)s).setChapterNumber(number);
    		}
    	}
    }

	/**
     * Returns the depth of this section.
     *
     * @return	the depth
     */
    public int getDepth() {
        return numbers.size();
    }
    
    // private methods
    
    /**
     * Sets the number of this section.
     *
     * @param	number		the number of this section
     * @param	numbers		an <CODE>ArrayList</CODE>, containing the numbers of the Parent
     */
    private void setNumbers(int number, ArrayList numbers) {
        this.numbers = new ArrayList();
        this.numbers.add(new Integer(number));
        this.numbers.addAll(numbers);
    }
    
    // deprecated stuff
    
    /**
	 * Returns the title, preceeded by a certain number of sectionnumbers.
	 *
	 * @return	a <CODE>Paragraph</CODE>
	 * @deprecated Use {@link #getTitle()} instead
	 */
	public Paragraph title() {
		return getTitle();
	}
    
    /**
	 * Returns the numberdepth of this <CODE>Section</CODE>.
	 *
	 * @return	the numberdepth
	 * @deprecated Use {@link #getNumberDepth()} instead
	 */
	public int numberDepth() {
		return getNumberDepth();
	}
    
    /**
	 * Returns the indentation of this <CODE>Section</CODE> on the left side.
	 *
	 * @return	the indentation
	 * @deprecated Use {@link #getIndentationLeft()} instead
	 */
	public float indentationLeft() {
		return getIndentationLeft();
	}
    
    /**
	 * Returns the indentation of this <CODE>Section</CODE> on the right side.
	 *
	 * @return	the indentation
	 * @deprecated Use {@link #getIndentationRight()} instead
	 */
	public float indentationRight() {
		return getIndentationRight();
	}
    
    /**
	 * Returns the indentation of the content of this <CODE>Section</CODE>.
	 *
	 * @return	the indentation
	 * @deprecated Use {@link #getIndentation()} instead
	 */
	public float indentation() {
		return getIndentation();
	}
    
    /**
	 * Returns the depth of this section.
	 *
	 * @return	the depth
	 * @deprecated Use {@link #getDepth()} instead
	 */
	public int depth() {
		return getDepth();
	}
    
    /**
     * Creates a given <CODE>Section</CODE> following a set of attributes and adds it to this one.
     *
     * @param	attributes	the attributes
     * @return      a Section
     * @deprecated Use ElementFactory.getSection(this, attributes)
     */
    public Section addSection(java.util.Properties attributes) {
    	return com.lowagie.text.factories.ElementFactory.getSection(this, attributes);
    }
}