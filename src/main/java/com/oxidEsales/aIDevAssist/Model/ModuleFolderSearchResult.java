package com.oxidEsales.aIDevAssist.Model;

public record ModuleFolderSearchResult (
    ComposerPSR4Entry composerPSR4Entry,
    String namespaceOfPhpFile,
    String absoluteModuleFolder
) {}
