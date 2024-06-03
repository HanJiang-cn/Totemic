- Fixed a recurring crash that happens when the Zaphkiel Waltz or Hymn of Maturity ceremony is performed near the bedrock or the build height limit, for example in Superflat worlds

API changes:
- Fixed CeremonyAPI.forEachBlockIn causing an exception when the given BoundingBox extends past the build height limit (causing the abovementioned crash)
