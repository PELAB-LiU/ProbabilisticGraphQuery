const path = require('path');

/**
 * import Setup2 from '!!../src/util/partial-loader?start=^static\ void\ [*]setup_main_stack[(]&end=^[}]!!@site/../example_solutions/2025/userprog/process.c';
 * 
 * @param {*} options 
 * @returns 
 */
function functionLoader(lines, options){
  var target = [];
  const start = new RegExp(options.start);
  const end = new RegExp(options.end);
  var idx = 0;
  while(idx<lines.length && !start.test(lines[idx])){
    idx++;
  }
  while(idx<lines.length && !end.test(lines[idx])){
    target.push(lines[idx]);
    idx++;
  }
  if(idx<lines.length){
    target.push(lines[idx]);
  }
  return target;
}

function lineLoader(lines, options){
  var target = [];
  options.lines.split(',').forEach(interval => {
    if (interval.includes('-')) {
      const numeric_interval = interval.replace('*',lines.length)

      // Parse start and end of the range
      const [start, end] = numeric_interval.split('-').map(num => parseInt(num.trim()));

      if (isNaN(start) || isNaN(end) || start <=0 || end <= 0 || start > end || end > lines.length) {
        throw new Error(`Invalid range: "${interval}".`);
      }

      // Collect elements within the range
      for (let i = start; i <= end; i++) {
        target.push(lines[i-1]);
      }
    } else {
      // Parse single index
      const index = parseInt(interval.trim())-1;
      if (isNaN(index)) {
        throw new Error(`Invalid index: "${interval}".`);
      }
      if (index >= 0 && index < lines.length) {
        target.push(lines[index]);
      }
    }
  });
  return target;
}

/**
 * A function to select specific lines from a raw text file.
 * Specify: ?lines=(line|(start-end))[,(line|(start-end))]+
 * @param {*} source 
 * @returns 
 */
module.exports = function (source) {
  // Split the content into lines and trim the first 5 lines
  const contentLines = source.toString().split(/\r?\n/);
  // Retrieve the query parameter (interval of lines to trim)
  const options = this.getOptions() || {};

  var target = [];

  if(options.start && options.end){
    target = functionLoader(contentLines, options);
  } else if(options.lines){
    target = lineLoader(contentLines, options);
  } else {
    target = contentLines;
  }

  // Join the remaining lines back together
  const trimmedContent = target.join('\n');

  // Return the processed content
  return `module.exports = ${JSON.stringify(trimmedContent)}`;
};

// Webpack needs to know that the loader is usable for text files
module.exports.raw = true; // Indicates that the loader can accept raw file buffers*/
