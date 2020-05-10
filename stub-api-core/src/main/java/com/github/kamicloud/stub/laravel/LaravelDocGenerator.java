package com.github.kamicloud.stub.laravel;

import com.github.kamicloud.stub.core.utils.CommentUtil;
import com.github.kamicloud.stub.core.utils.FileUtil;
import com.github.kamicloud.stub.core.utils.UrlUtil;
import com.github.kamicloud.stub.core.generators.BaseGenerator;
import com.github.kamicloud.stub.core.segments.common.FileCombiner;
import com.github.kamicloud.stub.core.segments.common.MultiLinesCombiner;
import definitions.annotations.*;
import com.github.kamicloud.stub.core.stubs.EnumStub;
import com.github.kamicloud.stub.core.stubs.OutputStub;
import com.github.kamicloud.stub.core.stubs.TemplateStub;
import com.github.kamicloud.stub.core.stubs.ParameterStub;
import definitions.annotations.Optional;

import java.io.*;
import java.util.*;

public class LaravelDocGenerator extends BaseGenerator {
    private File docPath;
    private File outputDir;
    private String docPrefix;

    @Override
    public void postConstruct() {
        docPath = new File(config.getGenerators().getLaravelDoc().getPath() + "/resources/docs");
        docPrefix = config.getGenerators().getLaravelDoc().getHttpPrefix();
    }

    @Override
    public void render(OutputStub output) {
        output.getTemplates().forEach((version, templateStub) -> {
            outputDir = new File(docPath.getAbsolutePath() + "/" + version);
            File generatedDir = new File(outputDir.getAbsolutePath() + "/generated");
            if (generatedDir.exists()) {
                FileUtil.deleteAllFilesOfDir(generatedDir);
            }
//            (new File(outputDir.getAbsolutePath() + "/generated/apis")).mkdirs();

            writeIndex(templateStub);
            writeOverview(version, templateStub);
            writeModels(templateStub);
            writeAPIs(version, templateStub);
            writeEnums(templateStub);
        });
        writeErrors(output);
    }

    private void writeIndex(TemplateStub o) {
        try {
            FileCombiner index = new FileCombiner();
            index.setFileName(outputDir.getAbsolutePath() + "/index.md");

            if (index.exists()) {
                return;
            }

            index.addBlock(new MultiLinesCombiner(
                "- ## Get Started",
                "  - [Overview](/" + docPrefix + "/{{version}}/overview)",
                "- ## 数据字典",
                "  - [ErrorCodes](/" + docPrefix + "/ErrorCodes)",
                "  - [Enums](/" + docPrefix + "/{{version}}/generated/enums)",
                "  - [Models](/" + docPrefix + "/{{version}}/generated/models)",
                "  - [API Overview](/" + docPrefix + "/{{version}}/generated/api-overview)",
                "- ## 接口文档"
            ));

            o.getControllers().forEach(controller -> {
                String comment = controller.getComment();
                index.addLine(
                    "  - [" + controller.getName() +
                    CommentUtil.getTitle(comment) +
                    "](/" + docPrefix +
                    "/{{version}}/generated/apis/" +
                    controller.getName() +
                    ")"
                );
            });

            index.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeOverview(String version, TemplateStub templateStub) {
        try {
            FileCombiner.build(
                outputDir.getAbsolutePath() + "/overview.md",
                templateStub.getComment(),
                false
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeAPIs(String version, TemplateStub output) {
        FileCombiner apiOverview = new FileCombiner();
        apiOverview.setFileName(outputDir.getAbsolutePath() + "/generated/api-overview.md");
        output.getControllers().forEach(controller -> {
            try {
                FileCombiner file = new FileCombiner();
                file.setFileName(outputDir.getAbsolutePath() + "/generated/apis/" + controller.getName() + ".md");

                String controllerTitle = "# " + controller.getName() + CommentUtil.getTitle(controller.getComment());

                file.addLine(controllerTitle);
                apiOverview.addLine("##" + controllerTitle);

                if (controller.hasCommentBody()) {
                    file.addMultiLines(
                        "",
                        "> {primary} " + controller.getBrCommentBody(),
                        ""
                    );
                }

                file.addMultiLines(
                    "",
                    "---",
                    ""
                );

                // 菜单列表
                controller.getActions().forEach((action) -> {
                    String actionName = action.getName().toString();

                    String comment = action.getComment();
                    comment = comment != null ? CommentUtil.getTitle(comment) : "";
                    if (action.hasAnnotation(Named.class)) {
                        actionName = action.getAnnotation(Named.class).getValue() + "@" + actionName;
                    }
                    String menu = "  - [" + actionName + comment + "](#" + action.getName() + ")";
                    String overviewMenu = "[`" + actionName + comment + "`](/" + docPrefix + "/{{version}}/generated/apis/" + controller.getName() + "#" + action.getName() + ")";
                    file.addLine(menu);

                    apiOverview.addLine(overviewMenu);
                    apiOverview.addLine();
                });

                file.addLine("");
                controller.getActions().forEach((action) -> {
                    String actionName = action.getName().toString();

                    file.addBlock(new MultiLinesCombiner(
                        "<a name=\"" + action.getName() + "\"></a>",
                        "## " + action.getName() + CommentUtil.getTitle(action.getComment())
                    ));
                    file.addLine("");
                    if (action.hasAnnotation(Methods.class)) {
                        file.addLine("`" + String.join("` `", action.getAnnotation(Methods.class).getValues()) + "`");
                    } else {
                        file.addLine("`POST`");
                    }
                    file.addLine("");
                    file.addLine("`" + UrlUtil.getUrlWithPrefix(version, controller.getName().toString(), actionName) + "`");
                    file.addLine("");
                    if (action.hasCommentBody()) {
                        file.addLine("\n> {primary} " + transformLfToBr(action.getComment()) + "\n");
                    }
                    writeParameters("Requests", file, action.getRequests());
                    writeParameters("Responses", file, action.getResponses());
                });
                file.toFile();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        try {
            apiOverview.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeErrors(OutputStub output) {
        try {
            FileCombiner overview = new FileCombiner();
            overview.setFileName(docPath.getAbsolutePath() + "/ErrorCodes/overview.md");
            MultiLinesCombiner blocks = new MultiLinesCombiner();
            blocks.addLine("|ErrorCode|Key|Description|");
            blocks.addLine("|:-|:-|:-|");

            output.getErrors().forEach(error -> {
                String comment = error.getComment() == null ? "" : error.getComment().replace("\n", "<br>");
                // 输出模型每一个请求参数
                blocks.addLine(
                    "|" + error.getCode() +
                    "|" + error.getName().toUpperUnderscoreCase() +
                    "|" + comment + " |"
                );
            });

            overview.addBlock(blocks);

            FileCombiner index = new FileCombiner();
            index.setFileName(docPath.getAbsolutePath() + "/ErrorCodes/index.md");
            if (index.exists()) {
                index.addBlock(new MultiLinesCombiner(
                    "- ## Get Started",
                    "  - [Overview](/" + docPrefix + "/{{version}}/overview)\n"
                ));
                index.toFile();
            }


            overview.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeModels(TemplateStub output) {
        try {
            FileCombiner file = new FileCombiner();
            file.setFileName(outputDir.getAbsolutePath() + "/generated/models.md");

            output.getModels().forEach((model) -> {
                String modelName = model.getName().toString();

                String comment = model.getComment();
                comment = comment != null ? CommentUtil.getTitle(comment) : "";

                file.addLine("  - [" + (model.isResource() ? "`REST`" : "") + modelName + comment + "](#" + modelName + ")");
            });

            file.addBlock(new MultiLinesCombiner(""));

            output.getModels().forEach((model) -> {
                file.addBlock(new MultiLinesCombiner(
                    "<a name=\"" + model.getName() + "\"></a>",
                    "## " + (model.isResource() ? "`REST`" : "") + model.getName() + CommentUtil.getTitle(model.getComment())
                ));

                if (model.isResource()) {
                    file.addBlock(new MultiLinesCombiner(
                        "`/api/v1/restful/" + model.getName().toLowerUnderscoreCase() + "`[DOC](https://laravel.com/docs/master/controllers#resource-controllers)"
                    ));
                }

                if (model.hasCommentBody()) {
                    file.addBlock(new MultiLinesCombiner("\n> {primary} " + model.getComment() + "\n"));
                }
                writeParameters("Attributes", file, model.getParameters());
            });

            file.toFile();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeEnums(TemplateStub output) {
        try {
            FileCombiner file = new FileCombiner();
            file.setFileName(outputDir.getAbsolutePath() + "/generated/enums.md");

            output.getEnums().forEach((enumStub) -> {
                String enumName = enumStub.getName().toString();

                String comment = enumStub.getComment();
                comment = comment != null ? CommentUtil.getTitle(comment) : "";

                file.addLine("  - [" + enumName + comment + "](#" + enumName + ")");
            });

            file.addBlock(new MultiLinesCombiner(""));

            output.getEnums().stream().filter(enumStub -> {
                return !enumStub.hasAnnotation(Versionless.class);
            }).forEach(enumStub -> {
                file.addBlock(new MultiLinesCombiner(
                    "<a name=\"" + enumStub.getName() + "\"></a>",
                    "## " + enumStub.getName() + CommentUtil.getTitle(enumStub.getComment())
                ));

                if (enumStub.hasCommentBody()) {
                    file.addBlock(new MultiLinesCombiner(
                        "",
                        "> {primary} " + enumStub.getComment(),
                        ""
                    ));
                }
                file.addBlock(new MultiLinesCombiner(
                    "|Key|Value|Description|",
                    "|:-|:-|:-|"
                ));

                HashMap<String, EnumStub.EnumStubItem> enumItems = enumStub.getItems();

                enumItems.forEach((key, value) -> {
                    String valueName = enumStub.hasAnnotation(StringEnum.class) ? key : value.getName().toString();
                    file.addLine(
                        "|" + key + "|" + valueName + "|" + (value.getComment() == null ? " " : transformLfToBr(value.getComment())) + "|"
                    );
                });
            });
            file.addLine("");

            file.toFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param title 标题
     * @param file 输出流
     * @param parameters 参数
     */
    private void writeParameters(String title, FileCombiner file, LinkedList<ParameterStub> parameters) {
        if (parameters.isEmpty()) {
            return;
        }
        file.addBlock(new MultiLinesCombiner(
            "### " + title,
            "|Key|Description|Type|Required|",
            "|:-|:-|:-|:-|"
        ));
        parameters.forEach((parameter) -> {
            file.addBlock(new MultiLinesCombiner(
                "|" + parameter.getName() + " |" +
                    (parameter.getComment() == null ? " " : transformLfToBr(parameter.getComment())) +
                    writeLink(parameter) +
                    (parameter.hasAnnotation(Optional.class) ? " " : "true") + "|"
            ));
        });
        file.addLine("");
    }


    private String writeLink(ParameterStub parameter) {
        String arraySuffix = "";
        String type = parameter.getTypeSimpleName();

        if (parameter.isArray()) {
            arraySuffix = "[]";
        }

        if (parameter.isModel()) {
            return ("|[`Models." + type + arraySuffix + "`](/" + docPrefix + "/{{version}}/generated/models#" + type.replace("[]", "") + ")|");
        } else if (parameter.isEnum()) {
            return ("|[`Enums." + type + arraySuffix + "`](/" + docPrefix + "/{{version}}/generated/enums#" + type.replace("[]", "") + ")|");
        } else {
            return ("|`" + type + arraySuffix + "`|");
        }
    }

    private String transformLfToBr(String lf) {
        if (lf == null) {
            return "";
        }
        return lf.replace("\n", "<br>");
    }
}
