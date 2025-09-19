import seaborn as sns
from matplotlib import pyplot as plt
import seaborn as sns
from collections import ChainMap

# Configuration 
COLUMN_WIDTH_IN = 2.99213 #in, (76mm)
TEXT_WIDTH_IN = 6.29921 #in, (160 mm)

# Settings for matplotlib
PTL_BASE = {
    "axes.titlesize": 10,
    "axes.labelsize": 8,
    "xtick.labelsize": 6,
    "ytick.labelsize": 6,
    "figure.constrained_layout.use": True
}

PLT_COL = ChainMap({
    "figure.figsize": (COLUMN_WIDTH_IN,  (6/10)*COLUMN_WIDTH_IN)
}, PTL_BASE)

PLT_PG = ChainMap({
    "figure.figsize": (TEXT_WIDTH_IN,  (6/10)*TEXT_WIDTH_IN)
}, PTL_BASE)

# Settings for seaborn
SNS_BASE = {
    #"context": "notebook",
    "style": "whitegrid",
    "palette": "muted",
    "font": "DejaVu Sans",
    "font_scale": 1.0,
}
COLW = ChainMap({
},{"rc": PLT_COL}, SNS_BASE)

TEXTW = ChainMap({
}, {"rc": PLT_PG}, SNS_BASE)

def sns_col():
    sns.set_theme(**COLW)
def sns_page():
    sns.set_theme(**TEXTW)

def toSize(w=1,h=1, scale=1):
    figwidth, _ = plt.rcParams["figure.figsize"]
    figwidth *= scale   
    figheight = (h/w)*figwidth
    return {"figsize": (figwidth,figheight)}