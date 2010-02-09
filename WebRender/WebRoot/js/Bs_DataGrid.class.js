// The global array of objects that have been instanciated
if (!Bs_Objects) {var Bs_Objects = [];};

/**
*
* <b>Includes (+Dependences):</b>
* <code>
*   <script type="text/javascript" src="/_bsJavascript/core/lang/Bs_Misc.lib.js"></script>
*   <script type="text/javascript" src="/_bsJavascript/core/lang/Bs_Array.class.js"></script>
* </code>
*
*
*
* @author     andrej arn <andrej-at-blueshoes-dot-org>
* @author     hervé raffourt
* @package    javascript_components
* @subpackage datagrid
* @copyright  blueshoes.org
*/
function Bs_DataGrid() {

  /**
  * Header fix if true. See also height.
  * @access public
  * @var    bool bHeaderFix
  */
  this.bHeaderFix = true;

  /**
  * Table height fix. if table content is longer than height, a scrollbar is shown.
  * @access public
  * @var    int height
  */
  this.height = 600;

  /**
  * Table width. can be an int like 200 or a percent value like "100%".
  * @access public
  * @var    mixed width
  */
  this.width='100%';

  /**
  * path for the buttons.
  * @access public
  * @var    string buttonsDefaultPath
  * @see    vars this.buttonsWysiwyg, this.buttonsHtml, this.buttonsText
  */
  //this.buttonsDefaultPath = '/_bsImages/buttons/';
  this.buttonsDefaultPath = './image/';

  /**
  * Have a click on the row (tr) make the browser go to an URL.
  *
  * if for example you show rows from a db in an overview, and you want to
  * make the browser go to the record detail/edit page on a click on the row,
  * just specify the field here that holds the unique key.
  *
  * example:
  *   yourDataGrid.rowClick = new Object( {key:2, baseUrl:'/foo.php?do=edit&type=foo&ID='} );
  *   the browser goes to /foo.php?do=edit&type=foo&ID={the value from the data with index 2, so the 3rd element.}
  *
  * @access public
  * @var    object rowClick
  * @since  bs-4.6
  */
  this.rowClick;

  /**
  * if the case should be ignored when sorting alphanumerically.
  * that means that "alpha" and "ALPHA" are threated the same.
  * @access public
  * @param  bool sortIgnoreCase
  * @since  bs-4.6
  */
  this.sortIgnoreCase = true;

  /**
  * if no data is available (no rows) should we display a text notice about it?
  * @access public
  * @var    bool useNoDataText (default is false)
  * @see    noDataText
  * @since  bs-4.6
  */
  this.useNoDataText = false;

  /**
  * text to display if no data. see useNoDataText.
  * @access public
  * @var    bool noDataText
  * @see    useNoDataText
  * @since  bs-4.6
  */
  this.noDataText = 'No data.';

  /**
  * @access private
  * @var    int this._id
  */
  this._id;

  /**
  * Unique Object/Tag ID is initialized in the constuctor.
  * Based on this._id. Can be used in genarated JS-code as ID. Is set together
  * from the classname + this._id (see _constructor() code ).
  *
  * @access private
  * @var  string
  */
  this._objectId;

  /**
  * cached value, used for re-rendering (for example when sorting).
  * @access private
  * @var    string _tagId
  */
  this._tagId;

  /**
  * Header properties.
  * @access private
  * @var    array _header
  * @see    this.setHeaderProps()
  */
  this._header;

  /**
  * @access private
  * @var    array _td
  * @see    this.setDataProps()
  */
  this._td;

  /**
  * @access private
  * @var    array _data
  * @see    this.getData()
  */
  this._data;

  /**
  * array holding all the information about attached events.
  *
  * the structure can be like these:
  * 1) attach a function directly
  *    syntax:  _attachedEvents['eventName'] = yourFunctionName;
  * 2) attach some javascript code
  *    syntax:  _attachedEvents['eventName'] = "yourCode();";
  *    example: _attachedEvents['eventName'] = "alert('hi'); callSomething('foo');";
  *    just keep in mind that you cannot use vars in that code, because when it
  *    gets executed that will be another scope (unless the vars are global...)
  * 3) attach multiple things for the same event
  *    syntax:  _attachedEvents['eventName']    = new Array;
  *             _attachedEvents['eventName'][0] = yourFunctionName;
  *             _attachedEvents['eventName'][1] = "yourCode();";
  *
  * @access private
  * @var    array _attachedEvents (hash, see above)
  * @see    this.attachEvent();
  */
  this._attachedEvents;


  /**
  * the pseudo constructor.
  * @access private
  * @return void
  */
  this._constructor = function() {
    // Put this instance into the global object instance list
    this._id = Bs_Objects.length;
    Bs_Objects[this._id] = this;
    this._objectId = "Bs_DateGrid_"+this._id;
  }


  /**
  * Header properties.
  *
  * properties:
  *   text   => visible text
  *   align  => left, center or right
  *   title  => mouse over title tag
  *   sort   => 'numeric' or 'alpha', or bool false if not sortable.
  *   width  => for example "30%"
  *   nowrap => bool
  *
  * example:
  *   new Array( {text:'ID',align:'center',title:'It&#039;s Country ID',sort:'numeric',width:'10%'}, {text:'NAME of the country',align:'center',sort:'alpha',width:'90%'});
  *
  * needs to be called before rendering.
  * @access public
  * @param  array properties
  * @return void
  */
  this.setHeaderProps = function(properties) {
    this._header = properties;
  }


  /**
  * Set data properties, used to:
  *
  * properties:
  *   align    => left, center or right
  *   onclick  => a js function to call. 2 params are given: the col number (starting at 1) and the data array of the row.
  *   hide     => boolean, default is false.
  *
  * example:
  *   apply a td align different to header align.
  *   example: new Array("{align:'left'}", "{align:'left'}")
  *
  * needs to be called before rendering.
  * @access public
  * @param  array properties
  * @return void
  */
  this.setDataProps = function(properties) {
    this._td = properties;
  }


  /**
  * Have a click on the row (tr) make the browser go to an URL.
  *
  * if for example you show rows from a db in an overview, and you want to
  * make the browser go to the record detail/edit page on a click on the row,
  * just specify the field here that holds the unique key.
  *
  * example:
  *   yourDataGrid.rowClick = new Object( {key:2, baseUrl:'/foo.php?do=edit&type=foo&ID='} );
  *   the browser goes to /foo.php?do=edit&type=foo&ID={the value from the data with index 2, so the 3rd element.}
  *
  * @access public
  * @var    object rowClick
  * @since  bs-4.6
  */
  this.rowClick;



  /**
  * needs to be called before rendering.
  * @access public
  * @param  array data
  * @return void
  */
  this.setData = function(data) {
    this._data = data;
  }


  /**
  * returns the data as array.
  * @access public
  * @return array
  */
  this.getData = function() {
    return this._data;
  }


  /**
  * returns the number of rows we have.
  * @access public
  * @return int (0-n)
  * @since  bs-4.6
  */
  this.getNumRows = function() {
    return this._data.length;
  }


  /**
  * adds the given row.
  * @access public
  * @param  array row
  * @return void
  * @since  bs-4.6
  */
  this.addRow = function(row) {
    this._data[this._data.length] = row;
    if (typeof(bs_dg_globalColumn) == 'undefined') {
      this.drawInto(this._tagId);
    } else {
      this.orderByColumn(bs_dg_globalColumn);
    }
  }


  /**
  * removes the given specified.
  * @access public
  * @param  int rowNumber
  * @return bool
  * @since  bs-4.6
  */
  this.removeRow = function(rowNumber) {
    var status = this._data.deleteItem(rowNumber -1);
    if (status) { //re-render
      if (typeof(bs_dg_globalColumn) == 'undefined') {
        this.drawInto(this._tagId);
      } else {
        this.orderByColumn(bs_dg_globalColumn);
      }
    }
    return status;
  }


  /**
  * switch sort desc or asc
  *
  * @access public
  * @return string
  * @see    switchSort()
  */
  this.switchSort = function() {
    if (this.bHeaderFix) {
      bs_dg_sort_asc = !bs_dg_sort_asc;
    }
  }

  /**
  * switch collapse or expend table
  *
  * @access public
  * @return string
  */
  this.switchOverflow = function() {
    if (this.bHeaderFix) {
      if (document.getElementById('bsTb' + this._objectId).style.overflow == 'auto') {
        this.expand();
      } else {
        this.collapse();
      }
    }
  }

  /**
  * collapse table accord height size
  *
  * @access public
  * @return string
  */
  this.collapse = function() {
    if (this.bHeaderFix) {
      document.getElementById('overflowButtonImg_' + this._id).src=this.buttonsDefaultPath+'expand.gif';
      document.getElementById('overflowButtonImg_' + this._id).alt='Expand';
      document.getElementById('bsTb' + this._objectId).style.overflow = 'auto';
    }
  }

  /**
  * full expand table.
  *
  * @access public
  * @return string
  * @see    expand()
  */
  this.expand = function() {
    if (this.bHeaderFix) {
      document.getElementById('overflowButtonImg_' + this._id).src=this.buttonsDefaultPath+'collapse.gif';
      document.getElementById('overflowButtonImg_' + this._id).alt='Collapse';
      document.getElementById('bsTb' + this._objectId).style.overflow = 'visible';
    }
  }

  /**
  * Renders this component (generates html code).
  *
  * @access public
  * @return string
  * @see    drawInto()
  */
  this.render = function() {
    var out        = new Array();
    var tdSettings = new Array();

    // check if height is smaller than height screen
    if (this.height > screen.height) this.height = screen.height - 100;

    out[out.length] = '<table id="bsDg_table' + this._objectId + '" bs_dg_objectId="' + this._objectId + '" class="bsDg_table" cellspacing="0" cellpadding="2" width="'+this.width+'" border="0"';
    if (ie && this.bHeaderFix) {
      // header fix for ie
      //out[out.length] = ' bodyHeight="' + this.height + '"';
    }
    out[out.length] = ' headerCSS="bsDb_tr_header"';
    out[out.length] = '>';

    if (typeof(this._header) == 'object') {
      out[out.length] = '<thead><tr class="bsDb_tr_header">';

      for (var i=0; i<this._header.length; i++) {
        tdSettings[i] = new Array();

        if (typeof(this._header[i]) == 'object') {
          var text     = this._header[i]['text'];
          var hasProps = true;
        } else {
          var text     = this._header[i];
          var hasProps = false;
        }

        //is orderable, and how (natural, numeric, up/down, parseInt(), ...
        out[out.length] = '<td';
        if (typeof(this._header[i]['title']) != 'undefined') {
          var title = this._header[i]['title'];
          title = title.replace(/"/g,'\'');
          out[out.length] = ' title="' + title + '"';
        }

        out[out.length] = ' id="' + this._objectId + '_title_td_' + i + '"';
        out[out.length] = ' style="';
        if (this._header[i]['sort'] != false) {
          out[out.length] = 'cursor:hand;cursor:pointer;';
        }
        if (hasProps) {
          if (!bs_isEmpty(this._header[i]['align'])) {
            out[out.length] = 'text-align:' + this._header[i]['align'] + ';';
            tdSettings[i]['align'] = this._header[i]['align'];
            if (typeof(this._td) == 'object') {
              if (!bs_isEmpty(this._td[i]['align'])) tdSettings[i]['align'] = this._td[i]['align'];
            }
          }
          if (!bs_isEmpty(this._header[i]['width'])) {
            out[out.length] = 'width:' + this._header[i]['width'] + ';';
          }
        }
        out[out.length] = '"';

        if (!bs_isEmpty(this._header[i]['nowrap']) && this._header[i]['nowrap']) {
          out[out.length] = ' nowrap';
        }

        if (this._header[i]['sort'] != false) {
          out[out.length] = ' onclick="Bs_Objects['+this._id+'].orderByColumn(' + i + ');Bs_Objects['+this._id+'].switchSort();"';
        }
        out[out.length] = ' class="bsDb_td_header"';
        out[out.length] = '>' + text + '</td>';
      }

      out[out.length] = '</tr></thead>';

      out[out.length] = '<tbody id="bsTb' + this._objectId + '"';
      if (moz && this.bHeaderFix) {
        // header fix for Mozilla
        out[out.length] = ' style="overflow:auto; max-height:'+this.height+';">';
      } else {
        out[out.length] = '>';
      }
    }
    if (typeof(this._data) == 'object') {
      if (this._data.length == 0) {
        if (this.useNoDataText) {
          out[out.length] = '<tr><td colspan="100%">' + this.noDataText + '</td></tr>';
        }
      } else {
        for (var i=0; i<this._data.length; i++) {
          out[out.length] = '<tr';
          //out[out.length] = ' onmouseover="this.className=\'bsDg_tr_row_zebraover_' + (i % 2) + '\'"';
          out[out.length] = ' onmouseover="Bs_Objects['+this._id+'].onMouseOver(this, ' + (i+1) + ', ' + (i % 2) + ');"';
          //out[out.length] = ' onmouseout="this.className=\'bsDg_tr_row_zebra_' + (i % 2) + '\'"';
          out[out.length] = ' onmouseout="Bs_Objects['+this._id+'].onMouseOut(this, '   + (i+1) + ', ' + (i % 2) + ');"';
          out[out.length] = ' class="bsDg_tr_row_zebra_' + (i % 2) + '"';
          if (typeof(this.rowClick) != 'undefined') {
            out[out.length] = ' onclick="newTab(\'' + this.rowClick.baseUrl + this._data[i][this.rowClick.key] + '\');"';
          	out[out.length] = ' menu=menu"'+this._data[i][this.rowClick.key]+'" ';
          }
          out[out.length] = '>';
          for (var j=0; j<this._data[i].length; j++) {
            if ((typeof(this._td) != 'undefined') && this._td[j]['hide']) {
              continue;
            }
            var isClickable = false;

            if (bs_isNull(this._data[i][j])) continue;
            if (typeof(this._data[i][j]) == 'object') {
              var text = (typeof(this._data[i][j]['text']) != 'undefined') ? this._data[i][j]['text'] : '';
            } else if (typeof(this._data[i][j]) == 'undefined') {
              this._data[i][j] = '';
              var text = this._data[i][j];
            } else {
              var text = this._data[i][j];
            }
            out[out.length] = '<td';
			// menu rightClick;
			if (typeof(this.rightClick) != 'undefined') {
            	out[out.length] = ' menu="'+this.rightClick.MenuType +'_'+ this._data[i][this.rightClick.key]+'" ';
            }
            //dump(this._td);
            if ((typeof(this._td) != 'undefined') && (typeof(this._td[j]['onclick']) != 'undefined')) {
              isClickable = true;
              out[out.length] = ' onclick="' + this._td[j]['onclick'] + '(' + (i+1) + ');"';
            }

            if (typeof(this._data[i][j]['title']) != 'undefined') {
              var title = this._data[i][j]['title'];
              title = title.replace(/"/g,'\'');
              out[out.length] = ' title="' + title + '"';
            }

            out[out.length] = ' style="';

            if ((typeof(tdSettings[j]) != 'undefined') && !bs_isEmpty(tdSettings[j]['align'])) {
              out[out.length] = 'text-align:' + tdSettings[j]['align'] + ';';
            }
            if ((typeof(this.rowClick) != 'undefined') || isClickable) {
              out[out.length] = 'cursor:hand;cursor:pointer;';
            } else {
              out[out.length] = 'cursor:default;';
            }
            out[out.length] = '"';

            if (typeof(this._data[i][j]['onclick']) != 'undefined') {
              out[out.length] = ' onclick="' + this._data[i][j]['onclick'] + '"';
              style = ' style=cursor:hand;cursor:pointer;';
            } else {
              style = '';
            }

            var zebraRowTdClass = 'bsDg_td_row_zebra_' + (i % 2);
            out[out.length] = ' class="' + zebraRowTdClass + ' bsDg_row_' + i + ' bsDg_col_' + j + '"';
            out[out.length] = style;
            out[out.length] = '>';
            out[out.length] = (text.length == 0) ? '&nbsp;' : text;
            out[out.length] = '</td>';
          }
          out[out.length] = '</tr>' + "\n";
        }
      }
    }
    out[out.length] = '</tbody></table>';
    return out.join('');
  }


  /**
  * Renders the component and places it into the tag specified.
  * @access public
  * @param  string tagId id of the tag. (Hint: use a <div>.)
  * return  bool (success)
  */
  this.drawInto = function(tagId) {
    this._tagId = tagId;
    if (this.bHeaderFix) {
      overflowbuttonHtml = this._getFixButton('expand');
    } else {
      overflowbuttonHtml = '';
    }
    var tagElm = document.getElementById(tagId);
    if (!tagElm) return false;
    tagElm.innerHTML = overflowbuttonHtml + this.render();

    //after render stuff:
    var tblElm = document.getElementById('bsDg_table' + this._objectId);
    if (tblElm.offsetHeight > this.height) {
      if (ie && this.bHeaderFix) {
        tblElm.bodyHeight = this.height;
      }
    } else {
      if (this.bHeaderFix) document.getElementById('overflowButtonDiv_' + this._id).style.display = 'none';
    }
  }

  /**
  * re-renders the whole table, ordered by the column given.
  * @access public
  * @param  int column (0-n)
  * @return void
  */
  this.orderByColumn = function(column) {
    bs_dg_globalColumn = column;
    if ((typeof(this._header[column]['sort']) != 'undefined') && (this._header[column]['sort'] == 'numeric')) {
      bs_dg_sort = 'numeric';
    } else {
      bs_dg_sort = 'alpha';
    }
    bs_dg_sort_ignoreCase = this.sortIgnoreCase;
    this._data.sort(bs_datagrid_sort);

    this.drawInto(this._tagId);

    document.getElementById(this._objectId + '_title_td_' + column).className += ' bsDb_td_header_sort';
  }


  /**
  * old name for _getFixButton().
  * @access private
  */
  this.GetFixButton = function(buttonName) {
    this._getFixButton(buttonName);
  }

  /**
  * switch collapse or expend table
  *
  * @access private
  * @return string
  */
  this._getFixButton = function(buttonName) {
    var top   = (moz) ? '0' : '16';
    var styletag = '';

    // fixed width
    if (this.width.search('%') < 1) {
      top = '20';
      var dynawidth = parseInt(this.width);
      dynawidth = dynawidth-22;
      styletag='style="left:'+dynawidth+'; position:relative;';
    } else if (this.width == "100%") {
      styletag='style="float:right; position:relative;';
    } else {
      top = '0';
      styletag='style="left:0; position:relative;';
    }
    styletag += ' top:' + top + ';';
    styletag += '"';

    overflowbuttonHtml = '<span id="overflowButtonDiv_' + this._id + '" ' + styletag + '><a href="#" onclick="Bs_Objects['+this._id+'].switchOverflow();window.event.returnValue = false; return false;"><img id="overflowButtonImg_' + this._id + '" alt="Expand" src="'+this.buttonsDefaultPath+buttonName+'.gif" border="0"></a></span>';
    return overflowbuttonHtml;
  }


  /**
  * triggered automatically.
  * @access private
  * @param  elm trElm
  * @param  int rowNumber
  * @param  int colorI
  * @return void
  */
  this.onMouseOver = function(trElm, rowNumber, colorI) {
    trElm.className = 'bsDg_tr_row_zebraover_' + colorI;
    this.fireEvent('onMouseOver', rowNumber);
  }

  /**
  * triggered automatically.
  * @access private
  * @param  elm trElm
  * @param  int rowNumber
  * @param  int colorI
  * @return void
  */
  this.onMouseOut = function(trElm, rowNumber, colorI) {
    trElm.className = 'bsDg_tr_row_zebra_' + colorI;
    this.fireEvent('onMouseOut', rowNumber);
  }


  /**
  * attaches an event.
  *
  * the following triggers can be used:
  *   'onMouseOver'
  *   'onMouseOut'
  *
  * the events will be executed in the order they were registered.
  *
  * if an onBeforeXXX event you've attached returns bool FALSE, it
  * will stop executing any other attached events in that queue,
  * and it will quit. example: if you attach an onBeforeChange
  * event, and your code returns FALSE, the change won't be done
  * at all.
  *
  * examples:
  *   myObj.attachEvent('onBeforeChange', myFunction);
  *   then your function myFunction() receives one param, it is
  *   a reference to this object (myObj).
  *
  *   myObj.attachEvent('onBeforeChange', "if (true) return false;");
  *   this is an example with code attached that will be evaluated.
  *
  * @access public
  * @param  string trigger
  * @param  mixed  yourEvent (string (of code) or function)
  * @return void
  * @see    var this._attachedEvents
  */
  this.attachEvent = function(trigger, yourEvent) {
    if (typeof(this._attachedEvents) == 'undefined') {
      this._attachedEvents = new Array();
    }

    if (typeof(this._attachedEvents[trigger]) == 'undefined') {
      this._attachedEvents[trigger] = new Array(yourEvent);
    } else {
      this._attachedEvents[trigger][this._attachedEvents[trigger].length] = yourEvent;
    }
  }

  /**
  * tells if any event is attached for the trigger specified.
  * @access public
  * @param  string trigger
  * @return bool
  */
  this.hasEventAttached = function(trigger) {
    return (this._attachedEvents && this._attachedEvents[trigger]);
  }

  /**
  * fires the events for the trigger specified.
  * @access public (used internally but feel free to trigger events yourself...)
  * @param  string trigger
  * @param  mixed params
  * @return void
  */
  this.fireEvent = function(trigger, params) {
    if (this._attachedEvents && this._attachedEvents[trigger]) {
      var e = this._attachedEvents[trigger];
      if ((typeof(e) == 'string') || (typeof(e) == 'function')) {
        e = new Array(e);
      }
      for (var i=0; i<e.length; i++) {
        if (typeof(e[i]) == 'function') {
          var status = e[i](this, params);
        } else if (typeof(e[i]) == 'string') {
          var status = eval(e[i]);
        } //else murphy
        if (status == false) return false;
      }
    }
    return true;
  }


  this._constructor(); //call the constructor. needs to be at the end.
}


var bs_dg_globalColumn;
var bs_dg_sort;
var bs_dg_sort_asc=true;
var bs_dg_sort_ignoreCase=true;

function bs_datagrid_sort(valA, valB) {
  var myRet=0;
  //have to find the value for sorting
  var a = valA[bs_dg_globalColumn];
  var b = valB[bs_dg_globalColumn];

  // If value passed is an object, extract the value of it
  if (typeof(a) == 'object') {
    a = (typeof(a['order']) != 'undefined') ? a['order'] : a['text'];
  }
  if (typeof(b) == 'object') {
    b = (typeof(b['order']) != 'undefined') ? b['order'] : b['text'];
  }

  if (bs_dg_sort == 'numeric') {
    a = parseFloat(a);
    b = parseFloat(b);
  } else { // 'alpha'
    if (bs_dg_sort_ignoreCase) {
      a = a.toLowerCase();
      b = b.toLowerCase();
    }
  }
  if (a < b) {
    myRet = -1;
  } else if (a > b) {
    myRet = 1;
  } else {
    myRet = 0;
  }

  if (!bs_dg_sort_asc) {
    myRet = myRet * (-1);
  }

  return myRet;
}

