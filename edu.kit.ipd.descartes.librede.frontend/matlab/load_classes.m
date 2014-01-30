function [] = load_classes()

persistent loaded;

if isempty(loaded)
    basePath = fileparts(fileparts(mfilename('fullpath')));
    jars = dir(strcat(basePath, '/lib/plugins/*.jar'));
    dpath = javaclasspath('-dynamic');
    for i = 1:size(jars,1)
        if isempty(strfind(dpath, jars(i).name))
            javaaddpath(fullfile('../lib/plugins', jars(i).name));
        end
    end
    
    dlls = dir(strcat(basePath, '/lib/native/win32/*.dll'));
    for i = 1:size(dlls,1)
        java.lang.System.load(fullfile(basePath, '/lib/native/win32', dlls(i).name));
    end
    
    loaded = 1;
end

end