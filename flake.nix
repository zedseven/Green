{
  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";

  outputs = {
    self,
    nixpkgs,
  }: let
    system = "x86_64-linux";

    pkgs = import nixpkgs {
      inherit system;
    };
  in {
    devShells.${system}.default = pkgs.mkShell {
      packages = with pkgs; [
        ant
        jdk17 # Matches the Java package used by `processing`
        processing
      ];

      shellHook = ''
        # Required for building, to access the core library files of the Processing installation
        ln --symbolic --force --no-dereference --verbose "${pkgs.processing}/lib/app" "./.direnv/classpath-local"
      '';
    };
  };
}
