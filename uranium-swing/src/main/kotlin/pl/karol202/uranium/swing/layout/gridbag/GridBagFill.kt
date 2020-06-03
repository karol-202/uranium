package pl.karol202.uranium.swing.layout.gridbag

import java.awt.GridBagConstraints

enum class GridBagFill(val code: Int)
{
	NONE(GridBagConstraints.NONE),
	HORIZONTAL(GridBagConstraints.HORIZONTAL),
	VERTICAL(GridBagConstraints.VERTICAL),
	BOTH(GridBagConstraints.BOTH);

	companion object
	{
		fun fill(horizontal: Boolean, vertical: Boolean) = when
		{
			horizontal && vertical -> BOTH
			horizontal -> HORIZONTAL
			vertical -> VERTICAL
			else -> NONE
		}
	}
}
