from matplotlib import pyplot
import numpy
import scipy

import probscale
import seaborn


def scatterplot(infile, outfile):
    #fig = pyplot.figure()  # an empty figure with no axes
    #fig.suptitle('No axes on this figure')  # Add a title so we know which it is

    fig, ax = pyplot.subplots(1, 1)

    #seaborn.set()
    
    data = numpy.asarray([float(line) for line in open(infile, "r").readlines()])

    seaborn.scatterplot(data = data, ax = ax, s = 10, linewidth = .1)

    fig.savefig(outfile, bbox_inches="tight", format="eps")

def probplot(infile, outfile):
    #clear_bkgd = { "axes.facecolor" : "none", "figure.facecolor" : "none" }
    #seaborn.set(style="ticks", context="notebook", rc=clear_bkgd)
    #seaborn.catplot

    data = numpy.asarray([float(line) for line in open(infile, "r").readlines()])

    fig, ax = pyplot.subplots(1, 1)
    fig = probscale.probplot(
        data = data,
        ax = ax,
        dist = scipy.stats.poisson(mu=5.0),
        bestfit = True,
        #estimate_ci = True,
        line_kws = {
            "label" : "BF Line",
            "color" : "b",
        },
        scatter_kws = {
            "label" : "Observations",
            "markersize" : 1.0,
            "linewidth" : 0.5,
        },
        problabel = "Probability (%)"
    )

    ax.legend(loc = "lower right")

    #seaborn.despine(fig)

    fig.savefig(outfile, bbox_inches="tight", format="png")


#scatterplot("Poisson_Generator.txt", "/tmp/file.eps")
#probplot("Gaussian_Generator.txt", "/tmp/file.png")
probplot("Poisson_Generator.txt", "/tmp/file.png")
