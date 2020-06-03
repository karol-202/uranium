package pl.karol202.uranium.swing.layout.gridbag

import java.awt.GridBagConstraints

enum class GridBagAnchor(val code: Int)
{
	CENTER(GridBagConstraints.CENTER),

	NORTH(GridBagConstraints.NORTH),
	NORTHEAST(GridBagConstraints.NORTHEAST),
	EAST(GridBagConstraints.EAST),
	SOUTHEAST(GridBagConstraints.SOUTHEAST),
	SOUTH(GridBagConstraints.SOUTH),
	SOUTHWEST(GridBagConstraints.SOUTHWEST),
	WEST(GridBagConstraints.WEST),
	NORTHWEST(GridBagConstraints.NORTHWEST),

	PAGE_START(GridBagConstraints.PAGE_START),
	FIRST_LINE_END(GridBagConstraints.FIRST_LINE_END),
	LINE_END(GridBagConstraints.LINE_END),
	LAST_LINE_END(GridBagConstraints.LAST_LINE_END),
	PAGE_END(GridBagConstraints.PAGE_END),
	LAST_LINE_START(GridBagConstraints.LAST_LINE_START),
	LINE_START(GridBagConstraints.LINE_START),
	FIRST_LINE_START(GridBagConstraints.FIRST_LINE_START)
}
