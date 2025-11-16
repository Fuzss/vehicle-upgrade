# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [v21.10.2-1.21.10] - 2025-11-16

### Changed

- Support riding mounts on top of leaves blocks while still being able to pass through them at and above eye height
- The body equipment slot in the mount inventory screen now shows a proper background icon for wolves and happy ghasts
- Buff the horse swim speed to match skeleton horses underwater speed

### Fixed

- Fix opening mob inventories via sneak and right-clicking preventing proper lead interactions
- Fix the double tab option for mount and player inventory switching leaking some unintended keystrokes and breaking
  input boxes like creative mode search (only fixed on NeoForge for now)
- Fix mounts not properly rendering as translucent while any screen is open
- Fix passengers not being ejected from happy ghasts when removing their harness via the added inventory screen
- Implement workaround for a start-up crash occuring when installed together with the Moonrise mod

## [v21.10.1-1.21.10] - 2025-10-25

### Fixed

- Fix a start-up crash on dedicated servers

## [v21.10.0-1.21.10] - 2025-10-10

### Changed

- Update to Minecraft 1.21.10
