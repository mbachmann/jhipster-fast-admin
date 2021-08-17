import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ResourcePermissionService } from '../service/resource-permission.service';

import { ResourcePermissionComponent } from './resource-permission.component';

describe('Component Tests', () => {
  describe('ResourcePermission Management Component', () => {
    let comp: ResourcePermissionComponent;
    let fixture: ComponentFixture<ResourcePermissionComponent>;
    let service: ResourcePermissionService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResourcePermissionComponent],
      })
        .overrideTemplate(ResourcePermissionComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResourcePermissionComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ResourcePermissionService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.resourcePermissions?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
