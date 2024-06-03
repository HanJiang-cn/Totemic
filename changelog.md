- Fixed a recurring crash that happens when the Zaphkiel Waltz or Hymn of Maturity ceremony is performed near the bedrock or the build height limit, for example in Superflat worlds
- Internal changes to how the code is organized

API changes:
- Added new constructors to the Ceremony class which take two MusicInstruments (or Suppliers thereof) as arguments, rather than varargs. The varargs constructor has been deprecated.
- Added new constructors to (Portable)TotemCarving which take only one effect. The varargs constructor stays and is not deprecated.
- Added new constructors to PotionTotemEffect which take the MobEffect arguments directly rather than as Suppliers. The Supplier taking constructors have been deprecated.
- PotionTotemEffect is no longer storing the MobEffect as a Supplier, which means that the MobEffect has to exist at the time the PotionTotemEffect is constructed, possibly making it necessary to use a DeferredRegister or similar for the corresponding carving.
- Fixed CeremonyAPI.forEachBlockIn causing an exception when the given BoundingBox extends past the build height limit (causing the above mentioned crash)
