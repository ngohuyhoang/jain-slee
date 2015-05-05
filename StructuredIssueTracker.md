# Structured Issue Tracker #

For an easy development cycle the issues in this tracker follow a very simple yet strict structure, that matches the structure of the source repos.

For each version there is a Release issue, which is created and fixed by the project lead. This issue - e.g. https://code.google.com/p/jain-slee/issues/detail?id=52 - has a list of "blocked on" that links each Package Release issue. As expected no release can be done with a non fixed child issue.

The Package Release issue, serves as a hub to link each related Component Release child issue, again through the list of "blocked on" issues. The issue is created on demand by a core dev team member, after the first child issue is created. See an example at https://code.google.com/p/jain-slee/issues/detail?id=43

The Component Release issue, which is created on demand by a core developer team member, when first related issue is accepted (not created), serves as a hub to link each related concrete bug/feature issue, again through the list of "blocked on" issues. See an example at https://code.google.com/p/jain-slee/issues/detail?id=54

Finally, the standard issue with respect to a bug fix or feature development, which may be created by any user, should be supervised by a member of the core dev team, so that the related Component Release is aware about it. See an example at https://code.google.com/p/jain-slee/issues/detail?id=7

When creating an issue there are templates available for each level/type of issue, please use and abuse from these, and don't forget to follow instructions in it.

# Issue Labels #

Issue Labels are fundamental data in every issue. While one can navigate through the blocked/blocking links to have a full view of the issues involved with a release, the labels provide not only shortcuts on this, but also feed some tools of the dev team that auto generate stuff like roadmaps or release notes. Please use labels with care, according to the instructions on each issue template.

# Telscale #

Telscale issues are related to the stable and product ready SLEE releases, which have the foundations on the community code. These should be used only by the core dev team involved with the product. To learn more about Telscale and how it differs from Mobicents please check telestax.com