import fs from "node:fs/promises";
import browserslist from "browserslist";
import * as esbuild from "esbuild";
import * as lightning from "lightningcss";
import { exec } from "node:child_process";
import { promisify } from "util";

const execAsync = promisify(exec);

async function main() {
  await fs.mkdir("dist", { recursive: true });
  await compileTailwind();
  await css();
}

async function compileTailwind() {
  await execAsync('tailwindcss -i ./styles/styles.css -o dist/main.css');
}

async function css() {
  const l = browserslist("> 0.2%");
  const t = lightning.browserslistToTargets(l);
  const r = await lightning.bundleAsync({
    filename: "dist/main.css",
    minify: true,
    targets: t,
  });
  const c = String(r.code);
  await fs.writeFile("../resources/static/main.css", c);
}

await main();
