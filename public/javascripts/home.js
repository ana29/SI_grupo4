window.addEventListener('scroll', function () {
    document.body.classList.add('scrolled');
    if (window.scrollY == 0) {
        document.body.classList.remove('scrolled');
    }
    ;
});

var addFolder = function (folder) {
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

$(document).ready(function() {
    console.log("Document.ready executado");
    var $dialog = $('#dialog');
  var $dialog2 = $('#dialog2');

  var maskHeight = $(window).height();
    var maskWidth = $(window).width();

    $('#mask').css({'width': maskWidth, 'height': maskHeight});

    //Get the window height and width
    var winH = $(window).height();
    var winW = $(window).width();

    $dialog.css('top', winH / 2 - $dialog.height() / 2);
    $dialog.css('left', winW / 2 - $dialog.width() / 2);

  $dialog2.css('top',  winH/2-$dialog2.height()/2);
  $dialog2.css('left', winW/2-$dialog2.width()/2);

    $('.window .close').hide();

    $("#boxes").click(function (e) {
        e.preventDefault();
        $('#boxes2').hide();
        $('#mask').fadeIn();
        $('.window').fadeIn();
  });

  $("#share").click(function (e) {
    e.preventDefault();
    $('#mask').fadeIn();
    $('.window2').fadeIn();
        $('.close').fadeIn();
    });

  $(".close").click(function (e) {
    e.preventDefault();
        $("#mask").fadeOut();
        $('.window').fadeOut();
    });

    var $dialog2 = $('#dialog2');

    var maskHeight = $(document).height();
    var maskWidth = $(window).width();

    $('#mask2').css({'width': maskWidth, 'height': maskHeight});

    //Get the window height and width
    var winH = $(window).height();
    var winW = $(window).width();

    $dialog2.css('top', winH / 2 - $dialog2.height() / 2);
    $dialog2.css('left', winW / 2 - $dialog2.width() / 2);

    var IdDaPasta;

    $('.window2 .close2').hide();

    $(".botaoED").click(function (e) {
        IdDaPasta = $(this).closest('.thumbnail').find('h1').attr('id');
        e.preventDefault();
        $('#mask2').fadeIn();
        $('.window2').fadeIn();
        $('.close2').fadeIn();
    });

    $('#buttom2').click(function () {
        console.log("ENTROU AQUI")
        var antigoNome = $('#'+IdDaPasta).text();
        console.log(antigoNome);
        var antigo = $('#antigoNomePasta').val(antigoNome);
        console.log(antigo);
    });

    $(".close2").click(function () {
        $("#mask2").fadeOut();
        $('.window2').fadeOut();
    });
});

