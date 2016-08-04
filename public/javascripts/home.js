window.addEventListener('scroll', function() {
  document.body.classList.add('scrolled');
  if(window.scrollY == 0) {
    document.body.classList.remove('scrolled');
  };
});





var addFolder = function(folder) {
  var newFolder = document.createElement('div');
  newFolder.classList.add('folder');
  var iconFolder = document.createElement('i');
  iconFolder.classList.add('material-icons');
  var iconFolder_ = document.createTextNode('folder');
  iconFolder.appendChild(iconFolder_);
  var folderTooltip = document.createElement('span');
  var folderTooltip_ = document.createTextNode('0 Folders / 0 Files');
  folderTooltip.appendChild(folderTooltip_);
  iconFolder.appendChild(folderTooltip);
  newFolder.appendChild(iconFolder);
  var folderName = document.createElement('h1');
  var folderName_ = document.createTextNode(folder.name);
  folderName.appendChild(folderName_);
  newFolder.appendChild(folderName);
  document.getElementById('main').appendChild(newFolder);
};