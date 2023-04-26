package com.bit.bitcare.lucene;

import com.bit.bitcare.model.DiseaseDTO;
import org.apache.lucene.analysis.ko.KoreanAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class DiseaseIndexer {
    @Autowired
    private DiseaseRepository myRepository;

    public void index() throws IOException {
//        // 색인 디렉토리 생성
//        Directory directory = FSDirectory.open(Paths.get("/path/to/index"));
//
//        // 색인 작업
//        IndexWriterConfig config = new IndexWriterConfig(new KoreanAnalyzer());
//        IndexWriter indexWriter = new IndexWriter(directory, config);
//        List<DiseaseDTO> diseaseList = myRepository.findAll();
//        for (DiseaseDTO diseaseDTO : diseaseList) {
//            Document document = new Document();
//            document.add(new TextField("id", String.valueOf(diseaseDTO.getId()), Field.Store.YES));
//            document.add(new TextField("name", diseaseDTO.getName(), Field.Store.YES));
//            document.add(new TextField("code", diseaseDTO.getCode(), Field.Store.YES));
//
//            indexWriter.addDocument(document);
//        }
//
//        System.out.println(indexWriter.getDocStats());
//
//        indexWriter.close();
//        directory.close();
    }

    public List<DiseaseDTO> search(String queryString) throws IOException, ParseException {
        // 색인 디렉토리 열기
        Directory directory = FSDirectory.open(Paths.get("/path/to/index"));


        // 검색 쿼리 파싱
//        QueryParser parser = new QueryParser("content", new StandardAnalyzer());  // 쿼리가 하나일때 유사한데이터 가져오기
        
        // 쿼리랑 유사한 데이터들도 가져오기
        QueryParser parser = new MultiFieldQueryParser(new String[]{"name", "code"}, new KoreanAnalyzer());
        Query query = parser.parse(queryString);

        // 쿼리랑 일치한 데이터들만 가져오기
//        BooleanQuery.Builder builder = new BooleanQuery.Builder();
//        builder.add(new TermQuery(new Term("name", queryString)), BooleanClause.Occur.SHOULD);
//        builder.add(new TermQuery(new Term("code", queryString)), BooleanClause.Occur.SHOULD);
//        Query query = builder.build();

        System.out.println("query: " + query);    // 쿼리 내용 확인

        // 검색 실행
        IndexReader indexReader = DirectoryReader.open(directory);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query, 100);

        System.out.println(topDocs.scoreDocs.length); // 검색결과 갯수 확인

        // 검색 결과 매핑
        List<DiseaseDTO> myDtos = new ArrayList<>();
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document document = indexSearcher.doc(scoreDoc.doc);
            DiseaseDTO myDto = new DiseaseDTO();

            System.out.println(document.getFields());   // 검색 결과 확인

            myDto.setId(Integer.parseInt(document.get("id")));
            myDto.setName(document.get("name"));
            myDto.setCode(document.get("code"));
            myDtos.add(myDto);
        }


        indexReader.close();
        directory.close();

        return myDtos;
    }
}
