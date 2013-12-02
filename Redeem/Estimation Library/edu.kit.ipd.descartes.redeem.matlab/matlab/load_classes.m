function [] = load_classes()

persistent loaded;

if isempty(loaded)
    jars = dir('../lib/plugins/*.jar');
    dpath = javaclasspath('-dynamic');
    for i = 1:size(jars,1)
        if isempty(strfind(dpath, jars(i).name))
            javaaddpath(fullfile('../lib/plugins', jars(i).name));
        end
    end
    loaded = 1;
end

end